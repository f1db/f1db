"""Analytics and statistics API routes."""

from fastapi import APIRouter, Query, HTTPException
from api.database import query_all

router = APIRouter(prefix="/stats", tags=["Statistics & Records"])


@router.get("/records/drivers", response_model=list[dict])
def get_driver_records(
    category: str = Query(
        "wins",
        description="Record category: wins, poles, points, podiums, fastest_laps, championships, starts, grand_slams"
    ),
    limit: int = Query(25, ge=1, le=100, description="Number of results"),
):
    category_map = {
        "wins": "total_race_wins",
        "poles": "total_pole_positions",
        "points": "total_points",
        "podiums": "total_podiums",
        "fastest_laps": "total_fastest_laps",
        "championships": "total_championship_wins",
        "starts": "total_race_starts",
        "entries": "total_race_entries",
        "grand_slams": "total_grand_slams",
        "sprint_wins": "total_sprint_race_wins",
    }

    col = category_map.get(category)
    if not col:
        raise HTTPException(400, f"Invalid category. Valid: {', '.join(category_map.keys())}")

    return query_all(
        f"""SELECT id, name, nationality_country_id, {col} as value
            FROM driver WHERE {col} > 0
            ORDER BY {col} DESC LIMIT ?""",
        (limit,)
    )


@router.get("/records/constructors", response_model=list[dict])
def get_constructor_records(
    category: str = Query(
        "wins",
        description="Record category: wins, poles, points, podiums, fastest_laps, championships, 1_2_finishes"
    ),
    limit: int = Query(25, ge=1, le=100, description="Number of results"),
):
    category_map = {
        "wins": "total_race_wins",
        "poles": "total_pole_positions",
        "points": "total_points",
        "podiums": "total_podiums",
        "fastest_laps": "total_fastest_laps",
        "championships": "total_championship_wins",
        "starts": "total_race_starts",
        "1_2_finishes": "total_1_and_2_finishes",
    }

    col = category_map.get(category)
    if not col:
        raise HTTPException(400, f"Invalid category. Valid: {', '.join(category_map.keys())}")

    return query_all(
        f"""SELECT id, name, country_id, {col} as value
            FROM constructor WHERE {col} > 0
            ORDER BY {col} DESC LIMIT ?""",
        (limit,)
    )


@router.get("/head-to-head", response_model=dict)
def get_head_to_head(
    driver1: str = Query(..., description="First driver ID"),
    driver2: str = Query(..., description="Second driver ID"),
):
    """Compare two drivers in races where they both competed."""

    # Get driver names
    d1_info = query_all("SELECT id, name FROM driver WHERE id = ?", (driver1,))
    d2_info = query_all("SELECT id, name FROM driver WHERE id = ?", (driver2,))

    if not d1_info:
        raise HTTPException(404, f"Driver '{driver1}' not found")
    if not d2_info:
        raise HTTPException(404, f"Driver '{driver2}' not found")

    # Find races where both drivers have results
    shared_races = query_all(
        """SELECT r1.race_id,
           r1.position_number as driver1_position,
           r2.position_number as driver2_position,
           r1.points as driver1_points,
           r2.points as driver2_points
           FROM race_result r1
           JOIN race_result r2 ON r1.race_id = r2.race_id
           WHERE r1.driver_id = ? AND r2.driver_id = ?
           AND r1.position_number IS NOT NULL AND r2.position_number IS NOT NULL""",
        (driver1, driver2)
    )

    d1_wins = 0
    d2_wins = 0
    d1_total_points = 0.0
    d2_total_points = 0.0
    for r in shared_races:
        p1, p2 = r["driver1_position"], r["driver2_position"]
        if isinstance(p1, int) and isinstance(p2, int):
            if p1 < p2:
                d1_wins += 1
            elif p2 < p1:
                d2_wins += 1
        d1_total_points += float(r["driver1_points"] or 0)
        d2_total_points += float(r["driver2_points"] or 0)

    return {
        "driver1": d1_info[0],
        "driver2": d2_info[0],
        "total_shared_races": len(shared_races),
        "driver1_ahead": d1_wins,
        "driver2_ahead": d2_wins,
        "driver1_total_points": d1_total_points,
        "driver2_total_points": d2_total_points,
    }
