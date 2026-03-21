"""Driver API routes."""

from fastapi import APIRouter, Depends, HTTPException, Query
from api.database import query_all, query_one, query_paginated
from api.dependencies import PaginationParams
from api.models.common import PaginatedResponse
from api.models.drivers import Driver, DriverSummary, DriverFamilyRelationship, DriverSeasonResult
from api.models.results import RaceResult

router = APIRouter(prefix="/drivers", tags=["Drivers"])

DRIVER_SUMMARY_COLS = """d.id, d.name, d.first_name, d.last_name, d.abbreviation,
    d.permanent_number, d.gender, d.date_of_birth, d.nationality_country_id,
    d.total_race_wins, d.total_championship_wins, d.total_points"""


@router.get("", response_model=PaginatedResponse[DriverSummary])
def list_drivers(
    pagination: PaginationParams = Depends(),
    search: str | None = Query(None, description="Search by name"),
    nationality: str | None = Query(None, description="Filter by nationality country ID"),
    gender: str | None = Query(None, description="Filter by gender (MALE/FEMALE)"),
    sort_by: str = Query("name", description="Sort field"),
    order: str = Query("asc", description="Sort order (asc/desc)"),
):
    conditions = []
    params = []

    if search:
        conditions.append("(d.name LIKE ? OR d.first_name LIKE ? OR d.last_name LIKE ? OR d.full_name LIKE ?)")
        params.extend([f"%{search}%"] * 4)
    if nationality:
        conditions.append("d.nationality_country_id = ?")
        params.append(nationality)
    if gender:
        conditions.append("d.gender = ?")
        params.append(gender.upper())

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""

    allowed_sorts = {"name", "date_of_birth", "total_race_wins", "total_points",
                     "total_championship_wins", "total_podiums", "total_pole_positions"}
    sort_col = sort_by if sort_by in allowed_sorts else "name"
    sort_order = "DESC" if order.lower() == "desc" else "ASC"

    sql = f"SELECT {DRIVER_SUMMARY_COLS} FROM driver d {where} ORDER BY d.{sort_col} {sort_order}"
    count_sql = f"SELECT COUNT(*) FROM driver d {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/{driver_id}", response_model=Driver)
def get_driver(driver_id: str):
    row = query_one("SELECT * FROM driver WHERE id = ?", (driver_id,))
    if not row:
        raise HTTPException(404, "Driver not found")
    return row


@router.get("/{driver_id}/seasons", response_model=list[DriverSeasonResult])
def get_driver_seasons(driver_id: str):
    _check_driver(driver_id)
    return query_all(
        """SELECT year, position_number, total_race_entries, total_race_wins,
           total_points, total_podiums, total_pole_positions
           FROM season_driver WHERE driver_id = ? ORDER BY year""",
        (driver_id,)
    )


@router.get("/{driver_id}/results", response_model=list[RaceResult])
def get_driver_results(
    driver_id: str,
    season: int | None = Query(None, description="Filter by season year"),
    grand_prix_id: str | None = Query(None, description="Filter by grand prix ID"),
):
    _check_driver(driver_id)
    conditions = ["rr.driver_id = ?"]
    params: list = [driver_id]

    if season:
        conditions.append("r.year = ?")
        params.append(season)
    if grand_prix_id:
        conditions.append("r.grand_prix_id = ?")
        params.append(grand_prix_id)

    where = " AND ".join(conditions)
    return query_all(
        f"""SELECT rr.race_id, rr.position_number, rr.position_text, rr.driver_number,
            rr.driver_id, rr.constructor_id, rr.engine_manufacturer_id, rr.tyre_manufacturer_id,
            rr.laps, rr.time, rr.time_millis, rr.time_penalty, rr.gap, rr.gap_millis,
            rr.interval, rr.interval_millis, rr.reason_retired, rr.points,
            rr.grid_position_number, rr.positions_gained, rr.pit_stops,
            rr.fastest_lap, rr.driver_of_the_day, rr.grand_slam
            FROM race_result rr
            JOIN race r ON r.id = rr.race_id
            WHERE {where}
            ORDER BY r.date""",
        tuple(params)
    )


@router.get("/{driver_id}/standings", response_model=list[dict])
def get_driver_standings_history(
    driver_id: str,
    season: int | None = Query(None, description="Filter by season year"),
):
    _check_driver(driver_id)
    conditions = ["sds.driver_id = ?"]
    params: list = [driver_id]
    if season:
        conditions.append("sds.year = ?")
        params.append(season)

    where = " AND ".join(conditions)
    return query_all(
        f"""SELECT sds.year, sds.position_number, sds.position_text, sds.points, sds.championship_won
            FROM season_driver_standing sds WHERE {where} ORDER BY sds.year""",
        tuple(params)
    )


@router.get("/{driver_id}/family", response_model=list[DriverFamilyRelationship])
def get_driver_family(driver_id: str):
    _check_driver(driver_id)
    return query_all(
        """SELECT dfr.driver_id, dfr.other_driver_id, d.name as other_driver_name, dfr.type
           FROM driver_family_relationship dfr
           JOIN driver d ON d.id = dfr.other_driver_id
           WHERE dfr.driver_id = ?
           ORDER BY dfr.position_display_order""",
        (driver_id,)
    )


def _check_driver(driver_id: str):
    if not query_one("SELECT id FROM driver WHERE id = ?", (driver_id,)):
        raise HTTPException(404, "Driver not found")
