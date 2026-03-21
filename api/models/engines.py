"""Engine models."""

from pydantic import BaseModel


class EngineManufacturerSummary(BaseModel):
    id: str
    name: str
    country_id: str
    total_race_wins: int = 0
    total_championship_wins: int = 0


class EngineManufacturer(BaseModel):
    id: str
    name: str
    country_id: str
    best_championship_position: int | None = None
    best_starting_grid_position: int | None = None
    best_race_result: int | None = None
    total_championship_wins: int = 0
    total_race_entries: int = 0
    total_race_starts: int = 0
    total_race_wins: int = 0
    total_race_laps: int = 0
    total_podiums: int = 0
    total_podium_races: int = 0
    total_points: float = 0
    total_championship_points: float = 0
    total_pole_positions: int = 0
    total_fastest_laps: int = 0


class Engine(BaseModel):
    id: str
    engine_manufacturer_id: str
    name: str
    full_name: str
    capacity: float | None = None
    configuration: str | None = None
    aspiration: str | None = None
