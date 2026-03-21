"""Season models."""

from pydantic import BaseModel


class Season(BaseModel):
    year: int


class SeasonEntrant(BaseModel):
    year: int
    entrant_id: str
    entrant_name: str | None = None
    country_id: str
    constructor_id: str
    constructor_name: str | None = None
    engine_manufacturer_id: str
    driver_id: str
    driver_name: str | None = None
    rounds: str | None = None
    test_driver: bool = False
