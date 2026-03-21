"""Constructor API routes."""

from fastapi import APIRouter, Depends, HTTPException, Query
from api.database import query_all, query_one, query_paginated
from api.dependencies import PaginationParams
from api.models.common import PaginatedResponse
from api.models.constructors import Constructor, ConstructorSummary, ConstructorChronology

router = APIRouter(prefix="/constructors", tags=["Constructors"])


@router.get("", response_model=PaginatedResponse[ConstructorSummary])
def list_constructors(
    pagination: PaginationParams = Depends(),
    search: str | None = Query(None, description="Search by name"),
    country: str | None = Query(None, description="Filter by country ID"),
    sort_by: str = Query("name", description="Sort field"),
    order: str = Query("asc", description="Sort order"),
):
    conditions = []
    params = []

    if search:
        conditions.append("(c.name LIKE ? OR c.full_name LIKE ?)")
        params.extend([f"%{search}%"] * 2)
    if country:
        conditions.append("c.country_id = ?")
        params.append(country)

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""
    allowed_sorts = {"name", "total_race_wins", "total_points", "total_championship_wins"}
    sort_col = sort_by if sort_by in allowed_sorts else "name"
    sort_order = "DESC" if order.lower() == "desc" else "ASC"

    sql = f"""SELECT c.id, c.name, c.full_name, c.country_id,
              c.total_race_wins, c.total_championship_wins, c.total_points
              FROM constructor c {where} ORDER BY c.{sort_col} {sort_order}"""
    count_sql = f"SELECT COUNT(*) FROM constructor c {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/{constructor_id}", response_model=Constructor)
def get_constructor(constructor_id: str):
    row = query_one("SELECT * FROM constructor WHERE id = ?", (constructor_id,))
    if not row:
        raise HTTPException(404, "Constructor not found")
    return row


@router.get("/{constructor_id}/seasons", response_model=list[dict])
def get_constructor_seasons(constructor_id: str):
    _check_constructor(constructor_id)
    return query_all(
        """SELECT year, position_number, total_race_entries, total_race_wins,
           total_points, total_podiums, total_pole_positions
           FROM season_constructor WHERE constructor_id = ? ORDER BY year""",
        (constructor_id,)
    )


@router.get("/{constructor_id}/results", response_model=list[dict])
def get_constructor_results(
    constructor_id: str,
    season: int | None = Query(None, description="Filter by season year"),
):
    _check_constructor(constructor_id)
    conditions = ["rr.constructor_id = ?"]
    params: list = [constructor_id]
    if season:
        conditions.append("r.year = ?")
        params.append(season)

    where = " AND ".join(conditions)
    return query_all(
        f"""SELECT rr.race_id, rr.position_number, rr.position_text, rr.driver_number,
            rr.driver_id, rr.constructor_id, rr.engine_manufacturer_id,
            rr.laps, rr.time, rr.gap, rr.points, rr.reason_retired,
            rr.grid_position_number
            FROM race_result rr
            JOIN race r ON r.id = rr.race_id
            WHERE {where}
            ORDER BY r.date, rr.position_display_order""",
        tuple(params)
    )


@router.get("/{constructor_id}/drivers", response_model=list[dict])
def get_constructor_drivers(
    constructor_id: str,
    season: int | None = Query(None, description="Filter by season year"),
):
    _check_constructor(constructor_id)
    conditions = ["sed.constructor_id = ?"]
    params: list = [constructor_id]
    if season:
        conditions.append("sed.year = ?")
        params.append(season)

    where = " AND ".join(conditions)
    return query_all(
        f"""SELECT sed.year, sed.driver_id, d.name as driver_name,
            sed.rounds, sed.test_driver
            FROM season_entrant_driver sed
            JOIN driver d ON d.id = sed.driver_id
            WHERE {where}
            ORDER BY sed.year, d.name""",
        tuple(params)
    )


@router.get("/{constructor_id}/chronology", response_model=list[ConstructorChronology])
def get_constructor_chronology(constructor_id: str):
    _check_constructor(constructor_id)
    return query_all(
        """SELECT cc.constructor_id, cc.other_constructor_id, c.name as other_constructor_name,
           cc.year_from, cc.year_to
           FROM constructor_chronology cc
           JOIN constructor c ON c.id = cc.other_constructor_id
           WHERE cc.constructor_id = ?
           ORDER BY cc.position_display_order""",
        (constructor_id,)
    )


def _check_constructor(constructor_id: str):
    if not query_one("SELECT id FROM constructor WHERE id = ?", (constructor_id,)):
        raise HTTPException(404, "Constructor not found")
