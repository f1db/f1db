"""Constructor models."""

from pydantic import BaseModel


class ConstructorSummary(BaseModel):
    id: str
    name: str
    full_name: str
    country_id: str
    total_race_wins: int = 0
    total_championship_wins: int = 0
    total_points: float = 0


class Constructor(BaseModel):
    id: str
    name: str
    full_name: str
    country_id: str
    best_championship_position: int | None = None
    best_starting_grid_position: int | None = None
    best_race_result: int | None = None
    best_sprint_race_result: int | None = None
    total_championship_wins: int = 0
    total_race_entries: int = 0
    total_race_starts: int = 0
    total_race_wins: int = 0
    total_1_and_2_finishes: int = 0
    total_race_laps: int = 0
    total_podiums: int = 0
    total_podium_races: int = 0
    total_points: float = 0
    total_championship_points: float = 0
    total_pole_positions: int = 0
    total_fastest_laps: int = 0
    total_sprint_race_starts: int = 0
    total_sprint_race_wins: int = 0


class ConstructorChronology(BaseModel):
    constructor_id: str
    other_constructor_id: str
    other_constructor_name: str | None = None
    year_from: int
    year_to: int | None = None
