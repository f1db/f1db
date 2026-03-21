"""Miscellaneous API routes: continents, countries, grands prix, tyre manufacturers."""

from fastapi import APIRouter, Depends, HTTPException, Query
from api.database import query_all, query_one, query_paginated
from api.dependencies import PaginationParams
from api.models.common import PaginatedResponse
from api.models.misc import Continent, Country, GrandPrix, GrandPrixSummary, TyreManufacturer

router = APIRouter(tags=["Reference Data"])


# --- Continents ---

@router.get("/continents", response_model=list[Continent])
def list_continents():
    return query_all("SELECT * FROM continent ORDER BY name")


# --- Countries ---

@router.get("/countries", response_model=PaginatedResponse[Country])
def list_countries(
    pagination: PaginationParams = Depends(),
    continent: str | None = Query(None, description="Filter by continent ID"),
    search: str | None = Query(None, description="Search by name"),
):
    conditions = []
    params = []
    if continent:
        conditions.append("c.continent_id = ?")
        params.append(continent)
    if search:
        conditions.append("c.name LIKE ?")
        params.append(f"%{search}%")

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""
    sql = f"SELECT * FROM country c {where} ORDER BY c.name"
    count_sql = f"SELECT COUNT(*) FROM country c {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/countries/{country_id}", response_model=Country)
def get_country(country_id: str):
    row = query_one("SELECT * FROM country WHERE id = ?", (country_id,))
    if not row:
        raise HTTPException(404, "Country not found")
    return row


# --- Grands Prix ---

@router.get("/grand-prix", response_model=PaginatedResponse[GrandPrixSummary])
def list_grands_prix(
    pagination: PaginationParams = Depends(),
    search: str | None = Query(None, description="Search by name"),
):
    conditions = []
    params = []
    if search:
        conditions.append("(gp.name LIKE ? OR gp.full_name LIKE ?)")
        params.extend([f"%{search}%"] * 2)

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""
    sql = f"""SELECT gp.id, gp.name, gp.full_name, gp.abbreviation, gp.country_id, gp.total_races_held
              FROM grand_prix gp {where} ORDER BY gp.name"""
    count_sql = f"SELECT COUNT(*) FROM grand_prix gp {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/grand-prix/{gp_id}", response_model=dict)
def get_grand_prix(gp_id: str):
    gp = query_one("SELECT * FROM grand_prix WHERE id = ?", (gp_id,))
    if not gp:
        raise HTTPException(404, "Grand Prix not found")

    races = query_all(
        """SELECT r.id, r.year, r.round, r.date, r.circuit_id, r.official_name
           FROM race r WHERE r.grand_prix_id = ? ORDER BY r.date""",
        (gp_id,)
    )
    result = dict(gp)
    result["races"] = races
    return result


# --- Tyre Manufacturers ---

@router.get("/tyre-manufacturers", response_model=list[TyreManufacturer])
def list_tyre_manufacturers():
    return query_all("SELECT * FROM tyre_manufacturer ORDER BY name")


@router.get("/tyre-manufacturers/{tyre_id}", response_model=TyreManufacturer)
def get_tyre_manufacturer(tyre_id: str):
    row = query_one("SELECT * FROM tyre_manufacturer WHERE id = ?", (tyre_id,))
    if not row:
        raise HTTPException(404, "Tyre manufacturer not found")
    return row
