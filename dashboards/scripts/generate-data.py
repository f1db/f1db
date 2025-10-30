#!/usr/bin/env python3
"""
F1 Dashboard Data Generator

This script processes F1DB YAML data and generates optimized JSON files
for the React dashboards.
"""

import os
import json
import yaml
from pathlib import Path
from collections import defaultdict
from datetime import datetime

# Paths
SCRIPT_DIR = Path(__file__).parent
DASHBOARD_DIR = SCRIPT_DIR.parent
F1DB_DATA_DIR = DASHBOARD_DIR.parent / 'src' / 'data'
OUTPUT_DIR = DASHBOARD_DIR / 'src' / 'data'

def load_yaml(filepath):
    """Load and parse a YAML file."""
    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            return yaml.safe_load(f)
    except Exception as e:
        print(f"Error loading {filepath}: {e}")
        return None

def get_all_seasons():
    """Get list of all available seasons."""
    seasons_dir = F1DB_DATA_DIR / 'seasons'
    if not seasons_dir.exists():
        return []
    return sorted([d.name for d in seasons_dir.iterdir() if d.is_dir()])

def generate_driver_standings_history():
    """Generate historical driver championship data."""
    seasons = get_all_seasons()
    championships = []

    for season in seasons:
        standings_file = F1DB_DATA_DIR / 'seasons' / season / 'driver-standings.yml'
        if standings_file.exists():
            data = load_yaml(standings_file)
            if data and 'standings' in data and len(data['standings']) > 0:
                champion = data['standings'][0]
                championships.append({
                    'year': int(season),
                    'driverId': champion.get('driverId'),
                    'points': champion.get('points', 0)
                })

    return championships

def generate_constructor_standings_history():
    """Generate historical constructor championship data."""
    seasons = get_all_seasons()
    championships = []

    for season in seasons:
        standings_file = F1DB_DATA_DIR / 'seasons' / season / 'constructor-standings.yml'
        if standings_file.exists():
            data = load_yaml(standings_file)
            if data and 'standings' in data and len(data['standings']) > 0:
                champion = data['standings'][0]
                championships.append({
                    'year': int(season),
                    'constructorId': champion.get('constructorId'),
                    'points': champion.get('points', 0)
                })

    return championships

def generate_current_season_data():
    """Generate data for the current/latest season."""
    seasons = get_all_seasons()
    if not seasons:
        return None

    current_season = seasons[-1]
    season_dir = F1DB_DATA_DIR / 'seasons' / current_season

    # Driver standings
    driver_standings_file = season_dir / 'driver-standings.yml'
    driver_standings = []
    if driver_standings_file.exists():
        data = load_yaml(driver_standings_file)
        if data and 'standings' in data:
            driver_standings = data['standings'][:20]  # Top 20

    # Constructor standings
    constructor_standings_file = season_dir / 'constructor-standings.yml'
    constructor_standings = []
    if constructor_standings_file.exists():
        data = load_yaml(constructor_standings_file)
        if data and 'standings' in data:
            constructor_standings = data['standings'][:10]  # Top 10

    # Recent races
    races_dir = season_dir / 'races'
    recent_races = []
    if races_dir.exists():
        race_dirs = sorted([d for d in races_dir.iterdir() if d.is_dir()])
        for race_dir in race_dirs[-5:]:  # Last 5 races
            race_file = race_dir / 'race.yml'
            race_results_file = race_dir / 'race-results.yml'

            if race_file.exists() and race_results_file.exists():
                race_info = load_yaml(race_file)
                race_results = load_yaml(race_results_file)

                if race_info and race_results:
                    recent_races.append({
                        'raceName': race_info.get('grandPrixId'),
                        'date': race_info.get('date'),
                        'circuitId': race_info.get('circuitId'),
                        'winner': race_results.get('results', [{}])[0] if race_results.get('results') else None
                    })

    return {
        'season': int(current_season),
        'driverStandings': driver_standings,
        'constructorStandings': constructor_standings,
        'recentRaces': recent_races
    }

def generate_driver_stats():
    """Generate aggregated driver statistics."""
    drivers_dir = F1DB_DATA_DIR / 'drivers'
    if not drivers_dir.exists():
        return []

    driver_stats = []
    driver_files = list(drivers_dir.glob('*.yml'))[:100]  # Limit to 100 for performance

    for driver_file in driver_files:
        data = load_yaml(driver_file)
        if data:
            driver_stats.append({
                'id': data.get('id'),
                'firstName': data.get('firstName'),
                'lastName': data.get('lastName'),
                'countryId': data.get('countryId'),
                'totalRaces': data.get('totalRaceEntries', 0),
                'totalWins': data.get('totalRaceWins', 0),
                'totalPoles': data.get('totalRacePolePositions', 0),
                'totalChampionships': data.get('totalChampionshipWins', 0)
            })

    return sorted(driver_stats, key=lambda x: x['totalWins'], reverse=True)

def generate_circuits_data():
    """Generate circuit information."""
    circuits_dir = F1DB_DATA_DIR / 'circuits'
    if not circuits_dir.exists():
        return []

    circuits = []
    for circuit_file in circuits_dir.glob('*.yml'):
        data = load_yaml(circuit_file)
        if data:
            circuits.append({
                'id': data.get('id'),
                'name': data.get('name'),
                'countryId': data.get('countryId'),
                'totalRaces': data.get('totalRacesHeld', 0)
            })

    return circuits

def main():
    """Main data generation function."""
    print("F1 Dashboard Data Generator")
    print("=" * 50)

    # Ensure output directory exists
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)

    # Generate datasets
    print("\n1. Generating driver championships history...")
    driver_championships = generate_driver_standings_history()
    with open(OUTPUT_DIR / 'driver-championships.json', 'w') as f:
        json.dump(driver_championships, f, indent=2)
    print(f"   ✓ Generated {len(driver_championships)} records")

    print("\n2. Generating constructor championships history...")
    constructor_championships = generate_constructor_standings_history()
    with open(OUTPUT_DIR / 'constructor-championships.json', 'w') as f:
        json.dump(constructor_championships, f, indent=2)
    print(f"   ✓ Generated {len(constructor_championships)} records")

    print("\n3. Generating current season data...")
    current_season = generate_current_season_data()
    if current_season:
        with open(OUTPUT_DIR / 'current-season.json', 'w') as f:
            json.dump(current_season, f, indent=2)
        print(f"   ✓ Generated data for season {current_season['season']}")

    print("\n4. Generating driver statistics...")
    driver_stats = generate_driver_stats()
    with open(OUTPUT_DIR / 'driver-stats.json', 'w') as f:
        json.dump(driver_stats, f, indent=2)
    print(f"   ✓ Generated {len(driver_stats)} driver records")

    print("\n5. Generating circuits data...")
    circuits = generate_circuits_data()
    with open(OUTPUT_DIR / 'circuits.json', 'w') as f:
        json.dump(circuits, f, indent=2)
    print(f"   ✓ Generated {len(circuits)} circuit records")

    print("\n" + "=" * 50)
    print("✓ Data generation complete!")
    print(f"  Output directory: {OUTPUT_DIR}")

if __name__ == '__main__':
    main()
