"""Engine API routes."""

from fastapi import APIRouter, Depends, HTTPException, Query
from api.database import query_all, query_one, query_paginated
from api.dependencies import PaginationParams
from api.models.common import PaginatedResponse
from api.models.engines import Engine, EngineManufacturer, EngineManufacturerSummary

router = APIRouter(tags=["Engines"])


@router.get("/engine-manufacturers", response_model=PaginatedResponse[EngineManufacturerSummary])
def list_engine_manufacturers(
    pagination: PaginationParams = Depends(),
    search: str | None = Query(None, description="Search by name"),
    country: str | None = Query(None, description="Filter by country ID"),
):
    conditions = []
    params = []
    if search:
        conditions.append("em.name LIKE ?")
        params.append(f"%{search}%")
    if country:
        conditions.append("em.country_id = ?")
        params.append(country)

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""

    sql = f"""SELECT em.id, em.name, em.country_id, em.total_race_wins, em.total_championship_wins
              FROM engine_manufacturer em {where} ORDER BY em.name"""
    count_sql = f"SELECT COUNT(*) FROM engine_manufacturer em {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/engine-manufacturers/{manufacturer_id}", response_model=EngineManufacturer)
def get_engine_manufacturer(manufacturer_id: str):
    row = query_one("SELECT * FROM engine_manufacturer WHERE id = ?", (manufacturer_id,))
    if not row:
        raise HTTPException(404, "Engine manufacturer not found")
    return row


@router.get("/engine-manufacturers/{manufacturer_id}/engines", response_model=list[Engine])
def get_manufacturer_engines(manufacturer_id: str):
    if not query_one("SELECT id FROM engine_manufacturer WHERE id = ?", (manufacturer_id,)):
        raise HTTPException(404, "Engine manufacturer not found")
    return query_all(
        "SELECT * FROM engine WHERE engine_manufacturer_id = ? ORDER BY name",
        (manufacturer_id,)
    )


@router.get("/engines", response_model=PaginatedResponse[Engine])
def list_engines(
    pagination: PaginationParams = Depends(),
    manufacturer: str | None = Query(None, description="Filter by manufacturer ID"),
    configuration: str | None = Query(None, description="Filter by configuration (V6, V8, V10, etc.)"),
    aspiration: str | None = Query(None, description="Filter by aspiration type"),
):
    conditions = []
    params = []
    if manufacturer:
        conditions.append("e.engine_manufacturer_id = ?")
        params.append(manufacturer)
    if configuration:
        conditions.append("e.configuration = ?")
        params.append(configuration)
    if aspiration:
        conditions.append("e.aspiration = ?")
        params.append(aspiration)

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""

    sql = f"SELECT * FROM engine e {where} ORDER BY e.name"
    count_sql = f"SELECT COUNT(*) FROM engine e {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/engines/{engine_id}", response_model=Engine)
def get_engine(engine_id: str):
    row = query_one("SELECT * FROM engine WHERE id = ?", (engine_id,))
    if not row:
        raise HTTPException(404, "Engine not found")
    return row
