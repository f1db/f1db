"""Race API routes."""

from fastapi import APIRouter, Depends, HTTPException, Query
from api.database import query_all, query_one, query_paginated
from api.dependencies import PaginationParams
from api.models.common import PaginatedResponse
from api.models.races import Race, RaceSummary

router = APIRouter(prefix="/races", tags=["Races"])


@router.get("", response_model=PaginatedResponse[RaceSummary])
def list_races(
    pagination: PaginationParams = Depends(),
    season: int | None = Query(None, description="Filter by season year"),
    grand_prix_id: str | None = Query(None, description="Filter by grand prix ID"),
    circuit_id: str | None = Query(None, description="Filter by circuit ID"),
    date_from: str | None = Query(None, description="Filter by start date (YYYY-MM-DD)"),
    date_to: str | None = Query(None, description="Filter by end date (YYYY-MM-DD)"),
):
    conditions = []
    params = []

    if season:
        conditions.append("r.year = ?")
        params.append(season)
    if grand_prix_id:
        conditions.append("r.grand_prix_id = ?")
        params.append(grand_prix_id)
    if circuit_id:
        conditions.append("r.circuit_id = ?")
        params.append(circuit_id)
    if date_from:
        conditions.append("r.date >= ?")
        params.append(date_from)
    if date_to:
        conditions.append("r.date <= ?")
        params.append(date_to)

    where = f"WHERE {' AND '.join(conditions)}" if conditions else ""

    sql = f"""SELECT r.id, r.year, r.round, r.date, r.grand_prix_id, r.official_name, r.circuit_id
              FROM race r {where} ORDER BY r.date DESC"""
    count_sql = f"SELECT COUNT(*) FROM race r {where}"

    rows, total = query_paginated(sql, count_sql, tuple(params), pagination.limit, pagination.offset)
    return PaginatedResponse(data=rows, total=total, limit=pagination.limit, offset=pagination.offset)


@router.get("/{race_id}", response_model=Race)
def get_race(race_id: int):
    row = query_one("SELECT * FROM race WHERE id = ?", (race_id,))
    if not row:
        raise HTTPException(404, "Race not found")
    return row


@router.get("/{race_id}/results", response_model=list[dict])
def get_race_results(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT race_id, position_number, position_text, driver_number,
           driver_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id,
           laps, time, time_millis, time_penalty, gap, gap_millis, interval, interval_millis,
           reason_retired, points, pole_position, grid_position_number, grid_position_text,
           positions_gained, pit_stops, fastest_lap, driver_of_the_day, grand_slam
           FROM race_result WHERE race_id = ? ORDER BY position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/qualifying", response_model=list[dict])
def get_race_qualifying(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT race_id, position_number, position_text, driver_number,
           driver_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id,
           time, q1, q2, q3, gap, interval, laps
           FROM qualifying_result WHERE race_id = ? ORDER BY position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/grid", response_model=list[dict])
def get_race_grid(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT race_id, position_number, position_text, driver_number,
           driver_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id,
           qualification_position_number, grid_penalty, time
           FROM starting_grid_position WHERE race_id = ? ORDER BY position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/fastest-laps", response_model=list[dict])
def get_race_fastest_laps(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT race_id, position_number, position_text, driver_number,
           driver_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id,
           lap, time, time_millis, gap, interval
           FROM fastest_lap WHERE race_id = ? ORDER BY position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/pit-stops", response_model=list[dict])
def get_race_pit_stops(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT race_id, position_number, driver_number,
           driver_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id,
           stop, lap, time, time_millis
           FROM pit_stop WHERE race_id = ? ORDER BY position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/sprint", response_model=list[dict])
def get_race_sprint(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT race_id, position_number, position_text, driver_number,
           driver_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id,
           laps, time, time_millis, gap, gap_millis, interval, interval_millis,
           reason_retired, points, grid_position_number, positions_gained
           FROM sprint_race_result WHERE race_id = ? ORDER BY position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/driver-standings", response_model=list[dict])
def get_race_driver_standings(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT rds.race_id, rds.position_number, rds.position_text,
           rds.driver_id, d.name as driver_name, rds.points, rds.positions_gained
           FROM race_driver_standing rds
           JOIN driver d ON d.id = rds.driver_id
           WHERE rds.race_id = ? ORDER BY rds.position_display_order""",
        (race_id,)
    )


@router.get("/{race_id}/constructor-standings", response_model=list[dict])
def get_race_constructor_standings(race_id: int):
    _check_race(race_id)
    return query_all(
        """SELECT rcs.race_id, rcs.position_number, rcs.position_text,
           rcs.constructor_id, c.name as constructor_name,
           rcs.engine_manufacturer_id, rcs.points, rcs.positions_gained
           FROM race_constructor_standing rcs
           JOIN constructor c ON c.id = rcs.constructor_id
           WHERE rcs.race_id = ? ORDER BY rcs.position_display_order""",
        (race_id,)
    )


def _check_race(race_id: int):
    if not query_one("SELECT id FROM race WHERE id = ?", (race_id,)):
        raise HTTPException(404, "Race not found")
