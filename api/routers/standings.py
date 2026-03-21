"""Standings API routes."""

from fastapi import APIRouter, Query, HTTPException
from api.database import query_all
from api.models.standings import DriverStanding, ConstructorStanding, Champion

router = APIRouter(prefix="/standings", tags=["Standings"])


@router.get("/drivers", response_model=list[DriverStanding])
def get_driver_standings(
    season: int = Query(..., description="Season year (required)"),
):
    return query_all(
        """SELECT sds.position_number, sds.position_text,
           sds.driver_id, d.name as driver_name, sds.points, sds.championship_won
           FROM season_driver_standing sds
           JOIN driver d ON d.id = sds.driver_id
           WHERE sds.year = ? ORDER BY sds.position_display_order""",
        (season,)
    )


@router.get("/constructors", response_model=list[ConstructorStanding])
def get_constructor_standings(
    season: int = Query(..., description="Season year (required)"),
):
    return query_all(
        """SELECT scs.position_number, scs.position_text,
           scs.constructor_id, c.name as constructor_name,
           scs.engine_manufacturer_id, scs.points, scs.championship_won
           FROM season_constructor_standing scs
           JOIN constructor c ON c.id = scs.constructor_id
           WHERE scs.year = ? ORDER BY scs.position_display_order""",
        (season,)
    )


@router.get("/drivers/champions", response_model=list[Champion])
def get_driver_champions():
    return query_all(
        """SELECT sds.year, sds.driver_id as id, d.name, sds.points
           FROM season_driver_standing sds
           JOIN driver d ON d.id = sds.driver_id
           WHERE sds.championship_won = 1
           ORDER BY sds.year"""
    )


@router.get("/constructors/champions", response_model=list[Champion])
def get_constructor_champions():
    return query_all(
        """SELECT scs.year, scs.constructor_id as id, c.name, scs.points
           FROM season_constructor_standing scs
           JOIN constructor c ON c.id = scs.constructor_id
           WHERE scs.championship_won = 1
           ORDER BY scs.year"""
    )
