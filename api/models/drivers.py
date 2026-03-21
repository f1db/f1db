"""Driver models."""

from pydantic import BaseModel


class DriverSummary(BaseModel):
    id: str
    name: str
    first_name: str
    last_name: str
    abbreviation: str
    permanent_number: str | None = None
    gender: str
    date_of_birth: str
    nationality_country_id: str
    total_race_wins: int = 0
    total_championship_wins: int = 0
    total_points: float = 0


class Driver(BaseModel):
    id: str
    name: str
    first_name: str
    last_name: str
    full_name: str
    abbreviation: str
    permanent_number: str | None = None
    gender: str
    date_of_birth: str
    date_of_death: str | None = None
    place_of_birth: str
    country_of_birth_country_id: str
    nationality_country_id: str
    second_nationality_country_id: str | None = None
    best_championship_position: int | None = None
    best_starting_grid_position: int | None = None
    best_race_result: int | None = None
    best_sprint_race_result: int | None = None
    total_championship_wins: int = 0
    total_race_entries: int = 0
    total_race_starts: int = 0
    total_race_wins: int = 0
    total_race_laps: int = 0
    total_podiums: int = 0
    total_points: float = 0
    total_championship_points: float = 0
    total_pole_positions: int = 0
    total_fastest_laps: int = 0
    total_sprint_race_starts: int = 0
    total_sprint_race_wins: int = 0
    total_driver_of_the_day: int = 0
    total_grand_slams: int = 0


class DriverFamilyRelationship(BaseModel):
    driver_id: str
    other_driver_id: str
    other_driver_name: str | None = None
    type: str


class DriverSeasonResult(BaseModel):
    year: int
    position_number: int | None = None
    total_race_entries: int = 0
    total_race_wins: int = 0
    total_points: float = 0
    total_podiums: int = 0
    total_pole_positions: int = 0
