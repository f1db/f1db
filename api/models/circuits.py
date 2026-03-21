"""Circuit models."""

from pydantic import BaseModel


class CircuitLayout(BaseModel):
    id: str
    circuit_id: str
    effective: bool = False
    length: float
    turns: int


class CircuitSummary(BaseModel):
    id: str
    name: str
    full_name: str
    type: str
    country_id: str
    total_races_held: int = 0


class Circuit(BaseModel):
    id: str
    name: str
    full_name: str
    previous_names: str | None = None
    type: str
    direction: str
    place_name: str
    country_id: str
    latitude: float
    longitude: float
    length: float
    turns: int
    total_races_held: int = 0
    layouts: list[CircuitLayout] = []
