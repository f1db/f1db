"""Season API routes."""

from fastapi import APIRouter, HTTPException, Query
from api.database import query_all, query_one
from api.models.seasons import Season, SeasonEntrant

router = APIRouter(prefix="/seasons", tags=["Seasons"])


@router.get("", response_model=list[Season])
def list_seasons():
    return query_all("SELECT year FROM season ORDER BY year DESC")


@router.get("/{year}", response_model=dict)
def get_season(year: int):
    season = query_one("SELECT year FROM season WHERE year = ?", (year,))
    if not season:
        raise HTTPException(404, "Season not found")

    races = query_all(
        """SELECT id, round, date, grand_prix_id, official_name, circuit_id
           FROM race WHERE year = ? ORDER BY round""",
        (year,)
    )
    return {"year": year, "races": races}


@router.get("/{year}/races", response_model=list[dict])
def get_season_races(year: int):
    _check_season(year)
    return query_all(
        """SELECT id, round, date, time, grand_prix_id, official_name, circuit_id,
           qualifying_format, sprint_qualifying_format, course_length, laps, distance
           FROM race WHERE year = ? ORDER BY round""",
        (year,)
    )


@router.get("/{year}/entrants", response_model=list[SeasonEntrant])
def get_season_entrants(year: int):
    _check_season(year)
    return query_all(
        """SELECT sed.year, sed.entrant_id, e.name as entrant_name,
           se.country_id, sed.constructor_id, c.name as constructor_name,
           sed.engine_manufacturer_id,
           sed.driver_id, d.name as driver_name,
           sed.rounds, sed.test_driver
           FROM season_entrant_driver sed
           JOIN entrant e ON e.id = sed.entrant_id
           JOIN season_entrant se ON se.year = sed.year AND se.entrant_id = sed.entrant_id
           JOIN constructor c ON c.id = sed.constructor_id
           JOIN driver d ON d.id = sed.driver_id
           WHERE sed.year = ?
           ORDER BY e.name, d.name""",
        (year,)
    )


@router.get("/{year}/driver-standings", response_model=list[dict])
def get_season_driver_standings(year: int):
    _check_season(year)
    return query_all(
        """SELECT sds.position_number, sds.position_text,
           sds.driver_id, d.name as driver_name, sds.points, sds.championship_won
           FROM season_driver_standing sds
           JOIN driver d ON d.id = sds.driver_id
           WHERE sds.year = ? ORDER BY sds.position_display_order""",
        (year,)
    )


@router.get("/{year}/constructor-standings", response_model=list[dict])
def get_season_constructor_standings(year: int):
    _check_season(year)
    return query_all(
        """SELECT scs.position_number, scs.position_text,
           scs.constructor_id, c.name as constructor_name,
           scs.engine_manufacturer_id, scs.points, scs.championship_won
           FROM season_constructor_standing scs
           JOIN constructor c ON c.id = scs.constructor_id
           WHERE scs.year = ? ORDER BY scs.position_display_order""",
        (year,)
    )


def _check_season(year: int):
    if not query_one("SELECT year FROM season WHERE year = ?", (year,)):
        raise HTTPException(404, "Season not found")
