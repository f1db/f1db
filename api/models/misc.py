"""Miscellaneous models: Continent, Country, GrandPrix, TyreManufacturer."""

from pydantic import BaseModel


class Continent(BaseModel):
    id: str
    code: str
    name: str
    demonym: str


class Country(BaseModel):
    id: str
    alpha2_code: str
    alpha3_code: str
    ioc_code: str | None = None
    name: str
    demonym: str | None = None
    continent_id: str


class GrandPrixSummary(BaseModel):
    id: str
    name: str
    full_name: str
    abbreviation: str
    country_id: str | None = None
    total_races_held: int = 0


class GrandPrix(BaseModel):
    id: str
    name: str
    full_name: str
    short_name: str
    abbreviation: str
    country_id: str | None = None
    total_races_held: int = 0


class TyreManufacturer(BaseModel):
    id: str
    name: str
    country_id: str
    best_starting_grid_position: int | None = None
    best_race_result: int | None = None
    total_race_entries: int = 0
    total_race_starts: int = 0
    total_race_wins: int = 0
    total_race_laps: int = 0
    total_podiums: int = 0
    total_pole_positions: int = 0
    total_fastest_laps: int = 0
