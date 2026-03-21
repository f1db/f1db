"""Circuit API routes."""

from fastapi import APIRouter, Depends, HTTPException, Query
from api.database import query_all, query_one, query_paginated
from api.dependencies import PaginationParams
from api.models.common import PaginatedResponse
from api.models.circuits import Circuit, CircuitSummary

router = APIRouter(prefix="/circuits", tags=["Circuits"])


@router.get("", response_model=PaginatedResponse[CircuitSummary])
def list_circuits(
    pagination: PaginationParams = Depends(),
    search: str | None = Query(None, description="Search by name"),
    country: str | None = Query(None, description="Filter by country ID"),
    type: str | None = Query(None, description="Filter by type (RACE/STREET)"),
    sort_by: str = Query("name", description="Sort field"),
):
    conditions = []
    params = []

    if search:
        conditions.append("(c.name LIKE ? OR c.full_name LIKE ?)")
        params.extend([f"%{search}%"] * 2)
    if country:
        conditions.append("c.country_id = ?")
        params.append(country)
    if type:
        conditions.append("c.type = ?")
        params.append(type.upper())

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""
    allowed_sorts = {"name", "total_races_held", "country_id"}
    sort_col = sort_by if sort_by in allowed_sorts else "name"

    sql = f"""SELECT c.id, c.name, c.full_name, c.type, c.country_id, c.total_races_held
              FROM circuit c {where} ORDER BY c.{sort_col}"""
    count_sql = f"SELECT COUNT(*) FROM circuit c {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/{circuit_id}", response_model=Circuit)
def get_circuit(circuit_id: str):
    row = query_one("SELECT * FROM circuit WHERE id = ?", (circuit_id,))
    if not row:
        raise HTTPException(404, "Circuit not found")

    layouts = query_all(
        "SELECT * FROM circuit_layout WHERE circuit_id = ? ORDER BY id", (circuit_id,)
    )
    result = dict(row)
    result["layouts"] = layouts
    return result


@router.get("/{circuit_id}/races", response_model=list[dict])
def get_circuit_races(
    circuit_id: str,
    season: int | None = Query(None, description="Filter by season year"),
):
    if not query_one("SELECT id FROM circuit WHERE id = ?", (circuit_id,)):
        raise HTTPException(404, "Circuit not found")

    conditions = ["r.circuit_id = ?"]
    params: list = [circuit_id]
    if season:
        conditions.append("r.year = ?")
        params.append(season)

    where = " AND ".join(conditions)
    return query_all(
        f"""SELECT r.id, r.year, r.round, r.date, r.grand_prix_id, r.official_name
            FROM race r WHERE {where} ORDER BY r.date""",
        tuple(params)
    )
