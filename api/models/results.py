"""Race result models."""

from pydantic import BaseModel


class RaceResult(BaseModel):
    race_id: int
    position_number: int | None = None
    position_text: str
    driver_number: str
    driver_id: str
    constructor_id: str
    engine_manufacturer_id: str
    tyre_manufacturer_id: str
    laps: int | None = None
    time: str | None = None
    time_millis: int | None = None
    time_penalty: str | None = None
    gap: str | None = None
    gap_millis: int | None = None
    interval: str | None = None
    interval_millis: int | None = None
    reason_retired: str | None = None
    points: float | None = None
    grid_position_number: int | None = None
    positions_gained: int | None = None
    pit_stops: int | None = None
    fastest_lap: bool | None = None
    driver_of_the_day: bool | None = None
    grand_slam: bool | None = None


class QualifyingResult(BaseModel):
    race_id: int
    position_number: int | None = None
    position_text: str
    driver_number: str
    driver_id: str
    constructor_id: str
    engine_manufacturer_id: str
    tyre_manufacturer_id: str
    time: str | None = None
    q1: str | None = None
    q2: str | None = None
    q3: str | None = None
    gap: str | None = None
    laps: int | None = None


class FastestLap(BaseModel):
    race_id: int
    position_number: int | None = None
    position_text: str
    driver_number: str
    driver_id: str
    constructor_id: str
    engine_manufacturer_id: str
    tyre_manufacturer_id: str
    lap: int | None = None
    time: str | None = None
    time_millis: int | None = None
    gap: str | None = None
    interval: str | None = None


class PitStop(BaseModel):
    race_id: int
    position_number: int | None = None
    driver_number: str
    driver_id: str
    constructor_id: str
    stop: int | None = None
    lap: int | None = None
    time: str | None = None
    time_millis: int | None = None


class StartingGridPosition(BaseModel):
    race_id: int
    position_number: int | None = None
    position_text: str
    driver_number: str
    driver_id: str
    constructor_id: str
    engine_manufacturer_id: str
    tyre_manufacturer_id: str
    qualification_position_number: int | None = None
    grid_penalty: str | None = None
    time: str | None = None
