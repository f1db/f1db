"""Standings models."""

from pydantic import BaseModel


class DriverStanding(BaseModel):
    position_number: int | None = None
    position_text: str
    driver_id: str
    driver_name: str | None = None
    points: float = 0
    championship_won: bool = False


class ConstructorStanding(BaseModel):
    position_number: int | None = None
    position_text: str
    constructor_id: str
    constructor_name: str | None = None
    engine_manufacturer_id: str | None = None
    points: float = 0
    championship_won: bool = False


class Champion(BaseModel):
    year: int
    id: str
    name: str
    points: float = 0
