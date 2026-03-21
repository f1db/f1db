"""F1DB REST API — Comprehensive Formula 1 data API."""

from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from api.routers import drivers, constructors, circuits, races, seasons, standings, engines, misc, stats

app = FastAPI(
    title="F1DB API",
    description="Comprehensive Formula 1 data API covering all seasons from 1950 to present. "
                "Provides access to drivers, constructors, circuits, races, results, standings, and statistics.",
    version="1.0.0",
)

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["GET"],
    allow_headers=["*"],
)

# Register routers
PREFIX = "/api/v1"
app.include_router(drivers.router, prefix=PREFIX)
app.include_router(constructors.router, prefix=PREFIX)
app.include_router(circuits.router, prefix=PREFIX)
app.include_router(races.router, prefix=PREFIX)
app.include_router(seasons.router, prefix=PREFIX)
app.include_router(standings.router, prefix=PREFIX)
app.include_router(engines.router, prefix=PREFIX)
app.include_router(misc.router, prefix=PREFIX)
app.include_router(stats.router, prefix=PREFIX)


@app.get("/", tags=["Health"])
def root():
    return {
        "name": "F1DB API",
        "version": "1.0.0",
        "description": "Formula 1 Database API",
        "docs": "/docs",
    }


@app.get("/health", tags=["Health"])
def health():
    return {"status": "ok"}
