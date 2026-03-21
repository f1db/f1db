"""Race models."""

from pydantic import BaseModel


class RaceSummary(BaseModel):
    id: int
    year: int
    round: int
    date: str
    grand_prix_id: str
    official_name: str
    circuit_id: str


class Race(BaseModel):
    id: int
    year: int
    round: int
    date: str
    time: str | None = None
    grand_prix_id: str
    official_name: str
    qualifying_format: str
    sprint_qualifying_format: str | None = None
    circuit_id: str
    circuit_layout_id: str
    circuit_type: str
    direction: str
    course_length: float
    turns: int
    laps: int
    distance: float
    scheduled_laps: int | None = None
    scheduled_distance: float | None = None
    free_practice_1_date: str | None = None
    free_practice_1_time: str | None = None
    free_practice_2_date: str | None = None
    free_practice_2_time: str | None = None
    free_practice_3_date: str | None = None
    free_practice_3_time: str | None = None
    qualifying_date: str | None = None
    qualifying_time: str | None = None
    sprint_qualifying_date: str | None = None
    sprint_qualifying_time: str | None = None
    sprint_race_date: str | None = None
    sprint_race_time: str | None = None
