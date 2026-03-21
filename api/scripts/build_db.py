#!/usr/bin/env python3
"""Build SQLite database from F1DB YAML source data."""

import os
import re
import sqlite3
import sys
import yaml
from pathlib import Path

ROOT_DIR = Path(__file__).resolve().parent.parent.parent
DATA_DIR = ROOT_DIR / "src" / "data"
SCHEMA_FILE = ROOT_DIR / "build-logic" / "src" / "main" / "resources" / "sql" / "create_schema.sql"
DB_FILE = Path(__file__).resolve().parent.parent / "f1db.sqlite"


def time_to_millis(time_str: str | None) -> int | None:
    """Convert a time string like '1:32.608' or '2:13:23.600' or '+22.457' to milliseconds."""
    if not time_str:
        return None
    time_str = time_str.strip()
    if not time_str:
        return None

    negative = time_str.startswith("-")
    if time_str.startswith("+") or time_str.startswith("-"):
        time_str = time_str[1:]

    parts = time_str.split(":")
    try:
        if len(parts) == 3:
            h, m, s = parts
            ms = int(float(h) * 3600000 + float(m) * 60000 + float(s) * 1000)
        elif len(parts) == 2:
            m, s = parts
            ms = int(float(m) * 60000 + float(s) * 1000)
        else:
            ms = int(float(parts[0]) * 1000)
    except (ValueError, TypeError):
        return None

    return -ms if negative else ms


def load_yaml(filepath: Path) -> dict | list | None:
    """Load a YAML file, returning None if it doesn't exist."""
    if not filepath.exists():
        return None
    with open(filepath, "r", encoding="utf-8") as f:
        return yaml.safe_load(f)


def create_schema(conn: sqlite3.Connection):
    """Create database schema from SQL file (tables and indexes only, skip views)."""
    schema_sql = SCHEMA_FILE.read_text(encoding="utf-8")

    # Split by semicolons and execute CREATE TABLE and CREATE INDEX statements
    statements = schema_sql.split(";")
    for stmt in statements:
        stmt = stmt.strip()
        if not stmt:
            continue
        upper = stmt.upper()
        if upper.startswith("CREATE TABLE") or upper.startswith("CREATE INDEX"):
            conn.execute(stmt + ";")

    # Create views separately
    for stmt in statements:
        stmt = stmt.strip()
        if stmt.upper().startswith("CREATE VIEW"):
            conn.execute(stmt + ";")

    conn.commit()


def insert_continents(conn: sqlite3.Connection):
    """Insert continent data."""
    data_dir = DATA_DIR / "continents"
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            'INSERT INTO continent (id, code, name, demonym) VALUES (?, ?, ?, ?)',
            (d["id"], d["code"], d["name"], d["demonym"])
        )
    conn.commit()
    print(f"  Inserted continents")


def insert_countries(conn: sqlite3.Connection):
    """Insert country data."""
    data_dir = DATA_DIR / "countries"
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            'INSERT INTO country (id, alpha2_code, alpha3_code, ioc_code, name, demonym, continent_id) VALUES (?, ?, ?, ?, ?, ?, ?)',
            (d["id"], d["alpha2Code"], d["alpha3Code"], d.get("iocCode"), d["name"], d.get("demonym"), d["continentId"])
        )
    conn.commit()
    print(f"  Inserted countries")


def insert_drivers(conn: sqlite3.Connection):
    """Insert driver data."""
    data_dir = DATA_DIR / "drivers"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            '''INSERT INTO driver (id, name, first_name, last_name, full_name, abbreviation,
               permanent_number, gender, date_of_birth, date_of_death, place_of_birth,
               country_of_birth_country_id, nationality_country_id, second_nationality_country_id,
               best_championship_position, best_starting_grid_position, best_race_result, best_sprint_race_result,
               total_championship_wins, total_race_entries, total_race_starts, total_race_wins,
               total_race_laps, total_podiums, total_points, total_championship_points,
               total_pole_positions, total_fastest_laps, total_sprint_race_starts, total_sprint_race_wins,
               total_driver_of_the_day, total_grand_slams)
               VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
            (d["id"], d["name"], d["firstName"], d["lastName"], d["fullName"], d["abbreviation"],
             d.get("permanentNumber"), d["gender"], str(d["dateOfBirth"]), str(d["dateOfDeath"]) if d.get("dateOfDeath") else None,
             d["placeOfBirth"], d["countryOfBirthCountryId"], d["nationalityCountryId"],
             d.get("secondNationalityCountryId"),
             d.get("bestChampionshipPosition"), d.get("bestStartingGridPosition"),
             d.get("bestRaceResult"), d.get("bestSprintRaceResult"),
             d.get("totalChampionshipWins", 0), d.get("totalRaceEntries", 0),
             d.get("totalRaceStarts", 0), d.get("totalRaceWins", 0),
             d.get("totalRaceLaps", 0), d.get("totalPodiums", 0),
             d.get("totalPoints", 0), d.get("totalChampionshipPoints", 0),
             d.get("totalPolePositions", 0), d.get("totalFastestLaps", 0),
             d.get("totalSprintRaceStarts", 0), d.get("totalSprintRaceWins", 0),
             d.get("totalDriverOfTheDay", 0), d.get("totalGrandSlams", 0))
        )

        # Insert family relationships
        for idx, rel in enumerate(d.get("familyRelationships", [])):
            conn.execute(
                'INSERT INTO driver_family_relationship (driver_id, position_display_order, other_driver_id, type) VALUES (?, ?, ?, ?)',
                (d["id"], idx + 1, rel["driverId"], rel["type"])
            )
        count += 1

    conn.commit()
    print(f"  Inserted {count} drivers")


def insert_constructors(conn: sqlite3.Connection):
    """Insert constructor data."""
    data_dir = DATA_DIR / "constructors"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            '''INSERT INTO constructor (id, name, full_name, country_id,
               best_championship_position, best_starting_grid_position, best_race_result, best_sprint_race_result,
               total_championship_wins, total_race_entries, total_race_starts, total_race_wins,
               total_1_and_2_finishes, total_race_laps, total_podiums, total_podium_races,
               total_points, total_championship_points, total_pole_positions, total_fastest_laps,
               total_sprint_race_starts, total_sprint_race_wins)
               VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
            (d["id"], d["name"], d["fullName"], d["countryId"],
             d.get("bestChampionshipPosition"), d.get("bestStartingGridPosition"),
             d.get("bestRaceResult"), d.get("bestSprintRaceResult"),
             d.get("totalChampionshipWins", 0), d.get("totalRaceEntries", 0),
             d.get("totalRaceStarts", 0), d.get("totalRaceWins", 0),
             d.get("total1And2Finishes", 0), d.get("totalRaceLaps", 0),
             d.get("totalPodiums", 0), d.get("totalPodiumRaces", 0),
             d.get("totalPoints", 0), d.get("totalChampionshipPoints", 0),
             d.get("totalPolePositions", 0), d.get("totalFastestLaps", 0),
             d.get("totalSprintRaceStarts", 0), d.get("totalSprintRaceWins", 0))
        )

        # Insert chronology
        for idx, ch in enumerate(d.get("chronology", [])):
            conn.execute(
                'INSERT INTO constructor_chronology (constructor_id, position_display_order, other_constructor_id, year_from, year_to) VALUES (?, ?, ?, ?, ?)',
                (d["id"], idx + 1, ch["constructorId"], ch["yearFrom"], ch.get("yearTo"))
            )
        count += 1

    conn.commit()
    print(f"  Inserted {count} constructors")


def insert_chassis(conn: sqlite3.Connection):
    """Insert chassis data."""
    data_dir = DATA_DIR / "chassis"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            'INSERT INTO chassis (id, constructor_id, name, full_name) VALUES (?, ?, ?, ?)',
            (d["id"], d["constructorId"], d["name"], d["fullName"])
        )
        count += 1
    conn.commit()
    print(f"  Inserted {count} chassis")


def insert_engine_manufacturers(conn: sqlite3.Connection):
    """Insert engine manufacturer data."""
    data_dir = DATA_DIR / "engine-manufacturers"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            '''INSERT INTO engine_manufacturer (id, name, country_id,
               best_championship_position, best_starting_grid_position, best_race_result, best_sprint_race_result,
               total_championship_wins, total_race_entries, total_race_starts, total_race_wins,
               total_race_laps, total_podiums, total_podium_races,
               total_points, total_championship_points, total_pole_positions, total_fastest_laps,
               total_sprint_race_starts, total_sprint_race_wins)
               VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
            (d["id"], d["name"], d["countryId"],
             d.get("bestChampionshipPosition"), d.get("bestStartingGridPosition"),
             d.get("bestRaceResult"), d.get("bestSprintRaceResult"),
             d.get("totalChampionshipWins", 0), d.get("totalRaceEntries", 0),
             d.get("totalRaceStarts", 0), d.get("totalRaceWins", 0),
             d.get("totalRaceLaps", 0), d.get("totalPodiums", 0),
             d.get("totalPodiumRaces", 0),
             d.get("totalPoints", 0), d.get("totalChampionshipPoints", 0),
             d.get("totalPolePositions", 0), d.get("totalFastestLaps", 0),
             d.get("totalSprintRaceStarts", 0), d.get("totalSprintRaceWins", 0))
        )
        count += 1
    conn.commit()
    print(f"  Inserted {count} engine manufacturers")


def insert_engines(conn: sqlite3.Connection):
    """Insert engine data."""
    data_dir = DATA_DIR / "engines"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            '''INSERT INTO engine (id, engine_manufacturer_id, name, full_name, capacity, configuration, aspiration)
               VALUES (?, ?, ?, ?, ?, ?, ?)''',
            (d["id"], d["engineManufacturerId"], d["name"], d["fullName"],
             d.get("capacity"), d.get("configuration"), d.get("aspiration"))
        )
        count += 1
    conn.commit()
    print(f"  Inserted {count} engines")


def insert_tyre_manufacturers(conn: sqlite3.Connection):
    """Insert tyre manufacturer data."""
    data_dir = DATA_DIR / "tyre-manufacturers"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            '''INSERT INTO tyre_manufacturer (id, name, country_id,
               best_starting_grid_position, best_race_result, best_sprint_race_result,
               total_race_entries, total_race_starts, total_race_wins,
               total_race_laps, total_podiums, total_podium_races,
               total_pole_positions, total_fastest_laps,
               total_sprint_race_starts, total_sprint_race_wins)
               VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
            (d["id"], d["name"], d["countryId"],
             d.get("bestStartingGridPosition"), d.get("bestRaceResult"), d.get("bestSprintRaceResult"),
             d.get("totalRaceEntries", 0), d.get("totalRaceStarts", 0), d.get("totalRaceWins", 0),
             d.get("totalRaceLaps", 0), d.get("totalPodiums", 0), d.get("totalPodiumRaces", 0),
             d.get("totalPolePositions", 0), d.get("totalFastestLaps", 0),
             d.get("totalSprintRaceStarts", 0), d.get("totalSprintRaceWins", 0))
        )
        count += 1
    conn.commit()
    print(f"  Inserted {count} tyre manufacturers")


def insert_entrants(conn: sqlite3.Connection):
    """Insert entrant data."""
    data_dir = DATA_DIR / "entrants"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute('INSERT INTO entrant (id, name) VALUES (?, ?)', (d["id"], d["name"]))
        count += 1
    conn.commit()
    print(f"  Inserted {count} entrants")


def insert_circuits(conn: sqlite3.Connection):
    """Insert circuit data."""
    data_dir = DATA_DIR / "circuits"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        previous_names = ", ".join(d.get("previousNames", [])) if d.get("previousNames") else None
        conn.execute(
            '''INSERT INTO circuit (id, name, full_name, previous_names, type, direction,
               place_name, country_id, latitude, longitude, length, turns, total_races_held)
               VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)''',
            (d["id"], d["name"], d["fullName"], previous_names,
             d["type"], d["direction"], d["placeName"], d["countryId"],
             d["latitude"], d["longitude"], d["length"], d["turns"],
             d.get("totalRacesHeld", 0))
        )

        # Insert circuit layouts
        for layout in d.get("layouts", []):
            conn.execute(
                'INSERT INTO circuit_layout (id, circuit_id, effective, length, turns) VALUES (?, ?, ?, ?, ?)',
                (layout["id"], d["id"], layout.get("effective", False), layout["length"], layout["turns"])
            )
        count += 1
    conn.commit()
    print(f"  Inserted {count} circuits")


def insert_grands_prix(conn: sqlite3.Connection):
    """Insert grand prix data."""
    data_dir = DATA_DIR / "grands-prix"
    count = 0
    for f in sorted(data_dir.glob("*.yml")):
        d = load_yaml(f)
        conn.execute(
            '''INSERT INTO grand_prix (id, name, full_name, short_name, abbreviation, country_id, total_races_held)
               VALUES (?,?,?,?,?,?,?)''',
            (d["id"], d["name"], d["fullName"], d["shortName"], d["abbreviation"],
             d.get("countryId"), d.get("totalRacesHeld", 0))
        )
        count += 1
    conn.commit()
    print(f"  Inserted {count} grands prix")


def insert_seasons(conn: sqlite3.Connection):
    """Insert all season data."""
    seasons_dir = DATA_DIR / "seasons"
    season_years = sorted([int(d.name) for d in seasons_dir.iterdir() if d.is_dir() and d.name.isdigit()])

    for year in season_years:
        season_dir = seasons_dir / str(year)
        conn.execute('INSERT INTO season (year) VALUES (?)', (year,))

        # Insert entrants
        entrants_data = load_yaml(season_dir / "entrants.yml")
        if entrants_data:
            insert_season_entrants(conn, year, entrants_data)

        # Insert season driver standings
        driver_standings = load_yaml(season_dir / "driver-standings.yml")
        if driver_standings:
            insert_season_driver_standings(conn, year, driver_standings)

        # Insert season constructor standings
        constructor_standings = load_yaml(season_dir / "constructor-standings.yml")
        if constructor_standings:
            insert_season_constructor_standings(conn, year, constructor_standings)

        # Insert races
        races_dir = season_dir / "races"
        if races_dir.exists():
            race_dirs = sorted(races_dir.iterdir())
            for race_dir in race_dirs:
                if race_dir.is_dir():
                    insert_race(conn, year, race_dir)

    conn.commit()
    print(f"  Inserted {len(season_years)} seasons with races")


def _flatten_entrant_constructors(e: dict) -> list[dict]:
    """Flatten an entrant YAML entry into a list of (constructor_id, engine_manufacturer_id, chassis_ids, engine_ids, tyre_id, drivers) tuples.

    The YAML has multiple formats:
    1. Simple: constructorId, chassisId, engineId, drivers/driverId at top level
    2. Multiple constructors: constructors: [...] array with nested data
    3. Multiple chassis: chassis: [...] array
    4. Multiple engines: engines: [...] array
    """
    if "constructors" in e:
        # Multiple constructors under one entrant
        results = []
        for c in e["constructors"]:
            results.extend(_flatten_single_constructor(c, e.get("countryId")))
        return results
    else:
        return _flatten_single_constructor(e)


def _flatten_single_constructor(c: dict, fallback_country: str | None = None) -> list[dict]:
    """Flatten a single constructor entry."""
    constructor_id = c.get("constructorId", "")
    engine_manufacturer_id = c.get("engineManufacturerId", "")
    tyre_id = c.get("tyreManufacturerId")

    # Chassis: single chassisId or chassis array
    chassis_ids = []
    if "chassisId" in c:
        chassis_ids = [c["chassisId"]]
    elif "chassis" in c:
        chassis_ids = [ch["chassisId"] for ch in c["chassis"]]

    # Engines: single engineId or engines array
    engine_ids = []
    if "engineId" in c:
        engine_ids = [c["engineId"]]
    elif "engines" in c:
        engine_ids = [en["engineId"] for en in c["engines"]]

    # Drivers: drivers array or single driverId
    drivers = []
    if "drivers" in c:
        drivers = c["drivers"]
    elif "driverId" in c:
        drivers = [{"driverId": c["driverId"], "rounds": c.get("rounds"), "testDriver": c.get("testDriver", False)}]

    return [{
        "constructor_id": constructor_id,
        "engine_manufacturer_id": engine_manufacturer_id,
        "tyre_manufacturer_id": tyre_id,
        "chassis_ids": chassis_ids,
        "engine_ids": engine_ids,
        "drivers": drivers,
    }]


def insert_season_entrants(conn: sqlite3.Connection, year: int, entrants: list):
    """Insert season entrant data (entrants, constructors, chassis, engines, tyres, drivers)."""
    seen_entrants = set()
    for e in entrants:
        entrant_id = e["entrantId"]
        country_id = e.get("countryId", "")

        # season_entrant (unique per year+entrant)
        if (year, entrant_id) not in seen_entrants:
            seen_entrants.add((year, entrant_id))
            conn.execute(
                'INSERT OR IGNORE INTO season_entrant (year, entrant_id, country_id) VALUES (?, ?, ?)',
                (year, entrant_id, country_id)
            )

        # Flatten constructors
        for flat in _flatten_entrant_constructors(e):
            constructor_id = flat["constructor_id"]
            engine_manufacturer_id = flat["engine_manufacturer_id"]
            tyre_id = flat["tyre_manufacturer_id"]

            # season_entrant_constructor
            conn.execute(
                'INSERT OR IGNORE INTO season_entrant_constructor (year, entrant_id, constructor_id, engine_manufacturer_id) VALUES (?, ?, ?, ?)',
                (year, entrant_id, constructor_id, engine_manufacturer_id)
            )

            # season_entrant_chassis
            for chassis_id in flat["chassis_ids"]:
                conn.execute(
                    'INSERT OR IGNORE INTO season_entrant_chassis (year, entrant_id, constructor_id, engine_manufacturer_id, chassis_id) VALUES (?, ?, ?, ?, ?)',
                    (year, entrant_id, constructor_id, engine_manufacturer_id, chassis_id)
                )

            # season_entrant_engine
            for engine_id in flat["engine_ids"]:
                conn.execute(
                    'INSERT OR IGNORE INTO season_entrant_engine (year, entrant_id, constructor_id, engine_manufacturer_id, engine_id) VALUES (?, ?, ?, ?, ?)',
                    (year, entrant_id, constructor_id, engine_manufacturer_id, engine_id)
                )

            # season_entrant_tyre_manufacturer
            if tyre_id:
                conn.execute(
                    'INSERT OR IGNORE INTO season_entrant_tyre_manufacturer (year, entrant_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id) VALUES (?, ?, ?, ?, ?)',
                    (year, entrant_id, constructor_id, engine_manufacturer_id, tyre_id)
                )

            # season_entrant_driver
            for drv in flat["drivers"]:
                rounds_val = drv.get("rounds")
                rounds_str = str(rounds_val) if rounds_val is not None else None
                conn.execute(
                    '''INSERT OR IGNORE INTO season_entrant_driver
                       (year, entrant_id, constructor_id, engine_manufacturer_id, driver_id, rounds, rounds_text, test_driver)
                       VALUES (?, ?, ?, ?, ?, ?, ?, ?)''',
                    (year, entrant_id, constructor_id, engine_manufacturer_id,
                     drv["driverId"], rounds_str, rounds_str, drv.get("testDriver", False))
                )

            # season_constructor
            conn.execute(
                '''INSERT OR IGNORE INTO season_constructor
                   (year, constructor_id, position_number, position_text,
                    best_starting_grid_position, best_race_result, best_sprint_race_result,
                    total_race_entries, total_race_starts, total_race_wins,
                    total_1_and_2_finishes, total_race_laps, total_podiums, total_podium_races,
                    total_points, total_pole_positions, total_fastest_laps,
                    total_sprint_race_starts, total_sprint_race_wins)
                   VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
                (year, constructor_id, None, None, None, None, None, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            )

            # season_engine_manufacturer
            conn.execute(
                '''INSERT OR IGNORE INTO season_engine_manufacturer
                   (year, engine_manufacturer_id, position_number, position_text,
                    best_starting_grid_position, best_race_result, best_sprint_race_result,
                    total_race_entries, total_race_starts, total_race_wins,
                    total_race_laps, total_podiums, total_podium_races,
                    total_points, total_pole_positions, total_fastest_laps,
                    total_sprint_race_starts, total_sprint_race_wins)
                   VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
                (year, engine_manufacturer_id, None, None, None, None, None, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
            )

            # season_tyre_manufacturer
            if tyre_id:
                conn.execute(
                    '''INSERT OR IGNORE INTO season_tyre_manufacturer
                       (year, tyre_manufacturer_id,
                        best_starting_grid_position, best_race_result, best_sprint_race_result,
                        total_race_entries, total_race_starts, total_race_wins,
                        total_race_laps, total_podiums, total_podium_races,
                        total_pole_positions, total_fastest_laps,
                        total_sprint_race_starts, total_sprint_race_wins)
                       VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
                    (year, tyre_id, None, None, None, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                )

            # season_driver
            for drv in flat["drivers"]:
                conn.execute(
                    '''INSERT OR IGNORE INTO season_driver
                       (year, driver_id, position_number, position_text,
                        best_starting_grid_position, best_race_result, best_sprint_race_result,
                        total_race_entries, total_race_starts, total_race_wins,
                        total_race_laps, total_podiums, total_points,
                        total_pole_positions, total_fastest_laps,
                        total_sprint_race_starts, total_sprint_race_wins,
                        total_driver_of_the_day, total_grand_slams)
                       VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
                    (year, drv["driverId"], None, None, None, None, None, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
                )


def insert_season_driver_standings(conn: sqlite3.Connection, year: int, standings: list):
    """Insert season driver standings."""
    for idx, s in enumerate(standings):
        pos = s.get("position")
        pos_text = str(pos) if pos is not None else ""
        conn.execute(
            '''INSERT INTO season_driver_standing
               (year, position_display_order, position_number, position_text, driver_id, points, championship_won)
               VALUES (?, ?, ?, ?, ?, ?, ?)''',
            (year, idx + 1, pos, pos_text, s["driverId"], s.get("points", 0),
             pos == 1)
        )


def insert_season_constructor_standings(conn: sqlite3.Connection, year: int, standings: list):
    """Insert season constructor standings."""
    for idx, s in enumerate(standings):
        pos = s.get("position")
        pos_text = str(pos) if pos is not None else ""
        conn.execute(
            '''INSERT INTO season_constructor_standing
               (year, position_display_order, position_number, position_text,
                constructor_id, engine_manufacturer_id, points, championship_won)
               VALUES (?, ?, ?, ?, ?, ?, ?, ?)''',
            (year, idx + 1, pos, pos_text, s["constructorId"],
             s.get("engineManufacturerId", ""), s.get("points", 0), pos == 1)
        )


def insert_race(conn: sqlite3.Connection, year: int, race_dir: Path):
    """Insert a single race and all its associated data."""
    race_data = load_yaml(race_dir / "race.yml")
    if not race_data:
        return

    race_id = race_data["id"]

    conn.execute(
        '''INSERT INTO race (id, year, round, date, time, grand_prix_id, official_name,
           qualifying_format, sprint_qualifying_format, circuit_id, circuit_layout_id,
           circuit_type, direction, course_length, turns, laps, distance,
           scheduled_laps, scheduled_distance,
           drivers_championship_decider, constructors_championship_decider,
           pre_qualifying_date, pre_qualifying_time,
           free_practice_1_date, free_practice_1_time,
           free_practice_2_date, free_practice_2_time,
           free_practice_3_date, free_practice_3_time,
           free_practice_4_date, free_practice_4_time,
           qualifying_1_date, qualifying_1_time,
           qualifying_2_date, qualifying_2_time,
           qualifying_date, qualifying_time,
           sprint_qualifying_date, sprint_qualifying_time,
           sprint_race_date, sprint_race_time,
           warming_up_date, warming_up_time)
           VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)''',
        (race_id, year, race_data["round"],
         str(race_data["date"]), race_data.get("time"),
         race_data["grandPrixId"], race_data["officialName"],
         race_data["qualifyingFormat"], race_data.get("sprintQualifyingFormat"),
         race_data["circuitId"], race_data["circuitLayoutId"],
         race_data.get("circuitType", "RACE"), race_data.get("direction", "CLOCKWISE"),
         race_data["courseLength"], race_data["turns"],
         race_data["laps"], race_data["distance"],
         race_data.get("scheduledLaps"), race_data.get("scheduledDistance"),
         race_data.get("driversChampionshipDecider", False),
         race_data.get("constructorsChampionshipDecider", False),
         str(race_data["preQualifyingDate"]) if race_data.get("preQualifyingDate") else None,
         race_data.get("preQualifyingTime"),
         str(race_data["freePractice1Date"]) if race_data.get("freePractice1Date") else None,
         race_data.get("freePractice1Time"),
         str(race_data["freePractice2Date"]) if race_data.get("freePractice2Date") else None,
         race_data.get("freePractice2Time"),
         str(race_data["freePractice3Date"]) if race_data.get("freePractice3Date") else None,
         race_data.get("freePractice3Time"),
         str(race_data["freePractice4Date"]) if race_data.get("freePractice4Date") else None,
         race_data.get("freePractice4Time"),
         str(race_data["qualifying1Date"]) if race_data.get("qualifying1Date") else None,
         race_data.get("qualifying1Time"),
         str(race_data["qualifying2Date"]) if race_data.get("qualifying2Date") else None,
         race_data.get("qualifying2Time"),
         str(race_data["qualifyingDate"]) if race_data.get("qualifyingDate") else None,
         race_data.get("qualifyingTime"),
         str(race_data["sprintQualifyingDate"]) if race_data.get("sprintQualifyingDate") else None,
         race_data.get("sprintQualifyingTime"),
         str(race_data["sprintRaceDate"]) if race_data.get("sprintRaceDate") else None,
         race_data.get("sprintRaceTime"),
         str(race_data["warmingUpDate"]) if race_data.get("warmingUpDate") else None,
         race_data.get("warmingUpTime"))
    )

    # Map of YAML file -> race_data type
    race_data_files = {
        "pre-qualifying-results.yml": "PRE_QUALIFYING_RESULT",
        "free-practice-1-results.yml": "FREE_PRACTICE_1_RESULT",
        "free-practice-2-results.yml": "FREE_PRACTICE_2_RESULT",
        "free-practice-3-results.yml": "FREE_PRACTICE_3_RESULT",
        "free-practice-4-results.yml": "FREE_PRACTICE_4_RESULT",
        "qualifying-1-results.yml": "QUALIFYING_1_RESULT",
        "qualifying-2-results.yml": "QUALIFYING_2_RESULT",
        "qualifying-results.yml": "QUALIFYING_RESULT",
        "sprint-qualifying-results.yml": "SPRINT_QUALIFYING_RESULT",
        "sprint-starting-grid-positions.yml": "SPRINT_STARTING_GRID_POSITION",
        "sprint-race-results.yml": "SPRINT_RACE_RESULT",
        "warming-up-results.yml": "WARMING_UP_RESULT",
        "starting-grid-positions.yml": "STARTING_GRID_POSITION",
        "race-results.yml": "RACE_RESULT",
        "fastest-laps.yml": "FASTEST_LAP",
        "pit-stops.yml": "PIT_STOP",
        "driver-of-the-day-results.yml": "DRIVER_OF_THE_DAY_RESULT",
    }

    for filename, data_type in race_data_files.items():
        filepath = race_dir / filename
        records = load_yaml(filepath)
        if not records:
            continue

        for idx, r in enumerate(records):
            pos = r.get("position")
            pos_text = str(pos) if pos is not None else ""
            driver_number = str(r.get("driverNumber", "")) if r.get("driverNumber") is not None else ""

            # Build row based on type
            row = {
                "race_id": race_id,
                "type": data_type,
                "position_display_order": idx + 1,
                "position_number": pos,
                "position_text": pos_text,
                "driver_number": driver_number,
                "driver_id": r["driverId"],
                "constructor_id": r["constructorId"],
                "engine_manufacturer_id": r["engineManufacturerId"],
                "tyre_manufacturer_id": r["tyreManufacturerId"],
            }

            # Practice results
            if data_type in ("FREE_PRACTICE_1_RESULT", "FREE_PRACTICE_2_RESULT",
                            "FREE_PRACTICE_3_RESULT", "FREE_PRACTICE_4_RESULT", "WARMING_UP_RESULT"):
                t = r.get("time")
                row["practice_time"] = t
                row["practice_time_millis"] = time_to_millis(t)
                row["practice_gap"] = r.get("gap")
                row["practice_gap_millis"] = time_to_millis(r.get("gap"))
                row["practice_interval"] = r.get("interval")
                row["practice_interval_millis"] = time_to_millis(r.get("interval"))
                row["practice_laps"] = r.get("laps")

            # Qualifying results (also covers pre-qualifying, sprint qualifying)
            if data_type in ("PRE_QUALIFYING_RESULT", "QUALIFYING_1_RESULT", "QUALIFYING_2_RESULT",
                            "QUALIFYING_RESULT", "SPRINT_QUALIFYING_RESULT"):
                t = r.get("time")
                row["qualifying_time"] = t
                row["qualifying_time_millis"] = time_to_millis(t)
                row["qualifying_q1"] = r.get("q1")
                row["qualifying_q1_millis"] = time_to_millis(r.get("q1"))
                row["qualifying_q2"] = r.get("q2")
                row["qualifying_q2_millis"] = time_to_millis(r.get("q2"))
                row["qualifying_q3"] = r.get("q3")
                row["qualifying_q3_millis"] = time_to_millis(r.get("q3"))
                row["qualifying_gap"] = r.get("gap")
                row["qualifying_gap_millis"] = time_to_millis(r.get("gap"))
                row["qualifying_interval"] = r.get("interval")
                row["qualifying_interval_millis"] = time_to_millis(r.get("interval"))
                row["qualifying_laps"] = r.get("laps")

            # Starting grid positions (regular + sprint)
            if data_type in ("STARTING_GRID_POSITION", "SPRINT_STARTING_GRID_POSITION"):
                row["starting_grid_position_qualification_position_number"] = r.get("qualificationPosition")
                row["starting_grid_position_qualification_position_text"] = str(r.get("qualificationPosition", "")) if r.get("qualificationPosition") else None
                row["starting_grid_position_grid_penalty"] = r.get("gridPenalty")
                row["starting_grid_position_grid_penalty_positions"] = r.get("gridPenaltyPositions")
                t = r.get("time")
                row["starting_grid_position_time"] = t
                row["starting_grid_position_time_millis"] = time_to_millis(t)

            # Race results (regular + sprint)
            if data_type in ("RACE_RESULT", "SPRINT_RACE_RESULT"):
                row["race_shared_car"] = r.get("sharedCar", False)
                row["race_laps"] = r.get("laps")
                t = r.get("time")
                row["race_time"] = t
                row["race_time_millis"] = time_to_millis(t)
                tp = r.get("timePenalty")
                row["race_time_penalty"] = tp
                row["race_time_penalty_millis"] = time_to_millis(tp)
                row["race_gap"] = r.get("gap")
                row["race_gap_millis"] = time_to_millis(r.get("gap"))
                row["race_gap_laps"] = r.get("gapLaps")
                row["race_interval"] = r.get("interval")
                row["race_interval_millis"] = time_to_millis(r.get("interval"))
                row["race_reason_retired"] = r.get("reasonRetired")
                row["race_points"] = r.get("points")
                row["race_pole_position"] = r.get("polePosition", False)
                row["race_qualification_position_number"] = r.get("qualificationPosition")
                row["race_qualification_position_text"] = str(r.get("qualificationPosition", "")) if r.get("qualificationPosition") else None
                row["race_grid_position_number"] = r.get("gridPosition")
                row["race_grid_position_text"] = str(r.get("gridPosition", "")) if r.get("gridPosition") else None
                row["race_positions_gained"] = r.get("positionsGained")
                row["race_pit_stops"] = r.get("pitStops")
                row["race_fastest_lap"] = r.get("fastestLap", False)
                row["race_driver_of_the_day"] = r.get("driverOfTheDay", False)
                row["race_grand_slam"] = r.get("grandSlam", False)

            # Fastest laps
            if data_type == "FASTEST_LAP":
                row["fastest_lap_lap"] = r.get("lap")
                t = r.get("time")
                row["fastest_lap_time"] = t
                row["fastest_lap_time_millis"] = time_to_millis(t)
                row["fastest_lap_gap"] = r.get("gap")
                row["fastest_lap_gap_millis"] = time_to_millis(r.get("gap"))
                row["fastest_lap_interval"] = r.get("interval")
                row["fastest_lap_interval_millis"] = time_to_millis(r.get("interval"))

            # Pit stops
            if data_type == "PIT_STOP":
                row["pit_stop_stop"] = r.get("stop")
                row["pit_stop_lap"] = r.get("lap")
                t = r.get("time")
                row["pit_stop_time"] = t
                row["pit_stop_time_millis"] = time_to_millis(t)

            # Driver of the day
            if data_type == "DRIVER_OF_THE_DAY_RESULT":
                row["driver_of_the_day_percentage"] = r.get("percentage")

            # Build INSERT statement
            columns = list(row.keys())
            placeholders = ", ".join(["?"] * len(columns))
            col_names = ", ".join([f'"{c}"' for c in columns])
            values = [row[c] for c in columns]

            conn.execute(f'INSERT INTO race_data ({col_names}) VALUES ({placeholders})', values)

    # Race driver standings
    driver_standings = load_yaml(race_dir / "driver-standings.yml")
    if driver_standings:
        for idx, s in enumerate(driver_standings):
            pos = s.get("position")
            pos_text = str(pos) if pos is not None else ""
            conn.execute(
                '''INSERT INTO race_driver_standing
                   (race_id, position_display_order, position_number, position_text,
                    driver_id, points, positions_gained, championship_won)
                   VALUES (?, ?, ?, ?, ?, ?, ?, ?)''',
                (race_id, idx + 1, pos, pos_text,
                 s["driverId"], s.get("points", 0), s.get("positionsGained"), False)
            )

    # Race constructor standings
    constructor_standings = load_yaml(race_dir / "constructor-standings.yml")
    if constructor_standings:
        for idx, s in enumerate(constructor_standings):
            pos = s.get("position")
            pos_text = str(pos) if pos is not None else ""
            conn.execute(
                '''INSERT INTO race_constructor_standing
                   (race_id, position_display_order, position_number, position_text,
                    constructor_id, engine_manufacturer_id, points, positions_gained, championship_won)
                   VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)''',
                (race_id, idx + 1, pos, pos_text,
                 s["constructorId"], s.get("engineManufacturerId", ""),
                 s.get("points", 0), s.get("positionsGained"), False)
            )


def compute_career_stats(conn: sqlite3.Connection):
    """Compute career statistics for drivers, constructors, engine manufacturers, and tyre manufacturers from race data."""

    # Driver career stats
    conn.execute("""
        UPDATE driver SET
            total_race_entries = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_race_starts = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.race_laps > 0
            ), 0),
            total_race_wins = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number = 1
            ), 0),
            total_podiums = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number <= 3
            ), 0),
            total_pole_positions = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.race_pole_position = 1
            ), 0),
            total_fastest_laps = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.race_fastest_lap = 1
            ), 0),
            total_points = COALESCE((
                SELECT SUM(rd.race_points) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_race_laps = COALESCE((
                SELECT SUM(rd.race_laps) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_driver_of_the_day = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.race_driver_of_the_day = 1
            ), 0),
            total_grand_slams = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.race_grand_slam = 1
            ), 0),
            best_race_result = (
                SELECT MIN(rd.position_number) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number IS NOT NULL
            ),
            best_starting_grid_position = (
                SELECT MIN(rd.race_grid_position_number) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'RACE_RESULT'
                AND rd.race_grid_position_number IS NOT NULL
            ),
            total_sprint_race_starts = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'SPRINT_RACE_RESULT'
            ), 0),
            total_sprint_race_wins = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'SPRINT_RACE_RESULT'
                AND rd.position_number = 1
            ), 0),
            best_sprint_race_result = (
                SELECT MIN(rd.position_number) FROM race_data rd
                WHERE rd.driver_id = driver.id AND rd.type = 'SPRINT_RACE_RESULT'
                AND rd.position_number IS NOT NULL
            )
    """)

    # Driver championship stats
    conn.execute("""
        UPDATE driver SET
            total_championship_wins = COALESCE((
                SELECT COUNT(*) FROM season_driver_standing sds
                WHERE sds.driver_id = driver.id AND sds.championship_won = 1
            ), 0),
            best_championship_position = (
                SELECT MIN(sds.position_number) FROM season_driver_standing sds
                WHERE sds.driver_id = driver.id AND sds.position_number IS NOT NULL
            ),
            total_championship_points = COALESCE((
                SELECT SUM(sds.points) FROM season_driver_standing sds
                WHERE sds.driver_id = driver.id
            ), 0)
    """)
    print("  Updated driver stats")

    # Constructor career stats
    conn.execute("""
        UPDATE constructor SET
            total_race_entries = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_race_starts = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.race_laps > 0
            ), 0),
            total_race_wins = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number = 1
            ), 0),
            total_podiums = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number <= 3
            ), 0),
            total_pole_positions = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.race_pole_position = 1
            ), 0),
            total_fastest_laps = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.race_fastest_lap = 1
            ), 0),
            total_points = COALESCE((
                SELECT SUM(rd.race_points) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_race_laps = COALESCE((
                SELECT SUM(rd.race_laps) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
            ), 0),
            best_race_result = (
                SELECT MIN(rd.position_number) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number IS NOT NULL
            ),
            best_starting_grid_position = (
                SELECT MIN(rd.race_grid_position_number) FROM race_data rd
                WHERE rd.constructor_id = constructor.id AND rd.type = 'RACE_RESULT'
                AND rd.race_grid_position_number IS NOT NULL
            ),
            total_championship_wins = COALESCE((
                SELECT COUNT(*) FROM season_constructor_standing scs
                WHERE scs.constructor_id = constructor.id AND scs.championship_won = 1
            ), 0),
            best_championship_position = (
                SELECT MIN(scs.position_number) FROM season_constructor_standing scs
                WHERE scs.constructor_id = constructor.id AND scs.position_number IS NOT NULL
            ),
            total_championship_points = COALESCE((
                SELECT SUM(scs.points) FROM season_constructor_standing scs
                WHERE scs.constructor_id = constructor.id
            ), 0)
    """)
    print("  Updated constructor stats")

    # Engine manufacturer career stats
    conn.execute("""
        UPDATE engine_manufacturer SET
            total_race_entries = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_race_starts = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.race_laps > 0
            ), 0),
            total_race_wins = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number = 1
            ), 0),
            total_podiums = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number <= 3
            ), 0),
            total_points = COALESCE((
                SELECT SUM(rd.race_points) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_pole_positions = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.race_pole_position = 1
            ), 0),
            total_fastest_laps = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.race_fastest_lap = 1
            ), 0),
            best_race_result = (
                SELECT MIN(rd.position_number) FROM race_data rd
                WHERE rd.engine_manufacturer_id = engine_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number IS NOT NULL
            )
    """)
    print("  Updated engine manufacturer stats")

    # Tyre manufacturer career stats
    conn.execute("""
        UPDATE tyre_manufacturer SET
            total_race_entries = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
            ), 0),
            total_race_starts = COALESCE((
                SELECT COUNT(DISTINCT rd.race_id) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.race_laps > 0
            ), 0),
            total_race_wins = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number = 1
            ), 0),
            total_podiums = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number <= 3
            ), 0),
            total_pole_positions = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.race_pole_position = 1
            ), 0),
            total_fastest_laps = COALESCE((
                SELECT COUNT(*) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.race_fastest_lap = 1
            ), 0),
            best_race_result = (
                SELECT MIN(rd.position_number) FROM race_data rd
                WHERE rd.tyre_manufacturer_id = tyre_manufacturer.id AND rd.type = 'RACE_RESULT'
                AND rd.position_number IS NOT NULL
            )
    """)
    print("  Updated tyre manufacturer stats")

    conn.commit()


def main():
    """Build the SQLite database."""
    print(f"Building F1DB SQLite database...")
    print(f"  Data directory: {DATA_DIR}")
    print(f"  Schema file: {SCHEMA_FILE}")
    print(f"  Output: {DB_FILE}")

    # Remove existing database
    if DB_FILE.exists():
        DB_FILE.unlink()

    conn = sqlite3.connect(str(DB_FILE))
    conn.execute("PRAGMA journal_mode=WAL")
    conn.execute("PRAGMA synchronous=NORMAL")
    conn.execute("PRAGMA foreign_keys=OFF")  # Disable during bulk insert for performance

    try:
        print("\nCreating schema...")
        create_schema(conn)

        print("\nInserting data...")
        insert_continents(conn)
        insert_countries(conn)
        insert_drivers(conn)
        insert_constructors(conn)
        insert_chassis(conn)
        insert_engine_manufacturers(conn)
        insert_engines(conn)
        insert_tyre_manufacturers(conn)
        insert_entrants(conn)
        insert_circuits(conn)
        insert_grands_prix(conn)
        insert_seasons(conn)

        print("\nComputing career statistics...")
        compute_career_stats(conn)

        conn.execute("PRAGMA foreign_keys=ON")
        conn.commit()

        # Print summary
        cursor = conn.cursor()
        tables = ["continent", "country", "driver", "constructor", "chassis",
                   "engine_manufacturer", "engine", "tyre_manufacturer", "entrant",
                   "circuit", "grand_prix", "season", "race", "race_data",
                   "season_driver_standing", "season_constructor_standing",
                   "race_driver_standing", "race_constructor_standing"]
        print("\nDatabase summary:")
        for table in tables:
            cursor.execute(f'SELECT COUNT(*) FROM "{table}"')
            count = cursor.fetchone()[0]
            print(f"  {table}: {count} rows")

        print(f"\nDatabase built successfully: {DB_FILE}")
        print(f"Size: {DB_FILE.stat().st_size / 1024 / 1024:.1f} MB")

    except Exception as e:
        print(f"\nError: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)
    finally:
        conn.close()


if __name__ == "__main__":
    main()
