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
from datetime import datetime, date

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
            # Standings files are arrays directly, not objects with 'standings' key
            if data and isinstance(data, list) and len(data) > 0:
                champion = data[0]
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
            # Standings files are arrays directly, not objects with 'standings' key
            if data and isinstance(data, list) and len(data) > 0:
                champion = data[0]
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

    # Driver standings - standings files are arrays directly
    driver_standings_file = season_dir / 'driver-standings.yml'
    driver_standings = []
    if driver_standings_file.exists():
        data = load_yaml(driver_standings_file)
        if data and isinstance(data, list):
            driver_standings = data[:20]  # Top 20

    # Constructor standings - standings files are arrays directly
    constructor_standings_file = season_dir / 'constructor-standings.yml'
    constructor_standings = []
    if constructor_standings_file.exists():
        data = load_yaml(constructor_standings_file)
        if data and isinstance(data, list):
            constructor_standings = data[:10]  # Top 10

    # All races in the season
    races_dir = season_dir / 'races'
    all_races = []
    recent_races = []
    unique_drivers = set()
    
    if races_dir.exists():
        race_dirs = sorted([d for d in races_dir.iterdir() if d.is_dir()])
        for race_dir in race_dirs:
            race_file = race_dir / 'race.yml'
            race_results_file = race_dir / 'race-results.yml'

            if race_file.exists() and race_results_file.exists():
                race_info = load_yaml(race_file)
                race_results = load_yaml(race_results_file)

                if race_info and race_results:
                    # race_results is a list directly, not a dict with 'results' key
                    winner = race_results[0] if isinstance(race_results, list) and len(race_results) > 0 else None
                    race_data = {
                        'raceName': race_info.get('grandPrixId'),
                        'date': race_info.get('date'),
                        'circuitId': race_info.get('circuitId'),
                        'winner': winner
                    }
                    all_races.append(race_data)
                    
                    # Track unique drivers from all races
                    if race_results and isinstance(race_results, list):
                        for result in race_results:
                            driver_id = result.get('driverId')
                            if driver_id:
                                unique_drivers.add(driver_id)
        
        # Get last 5 races for recent races
        recent_races = all_races[-5:]

    return {
        'season': int(current_season),
        'driverStandings': driver_standings,
        'constructorStandings': constructor_standings,
        'totalRaces': len(all_races),
        'uniqueDrivers': len(unique_drivers),
        'recentRaces': recent_races
    }

def calculate_driver_stats_from_races():
    """Calculate driver statistics by iterating through all race results."""
    seasons = get_all_seasons()
    
    # Initialize stats for all drivers
    driver_stats = defaultdict(lambda: {
        'totalRaces': 0,
        'totalWins': 0,
        'totalPoles': 0,
        'totalChampionships': 0
    })
    
    # Track championship winners
    championship_winners = set()
    
    print("   Processing race data...")
    race_count = 0
    for season in seasons:
        # Check if this driver won the championship
        standings_file = F1DB_DATA_DIR / 'seasons' / season / 'driver-standings.yml'
        if standings_file.exists():
            data = load_yaml(standings_file)
            if data and isinstance(data, list) and len(data) > 0:
                champion_id = data[0].get('driverId')
                if champion_id:
                    championship_winners.add((champion_id, int(season)))
        
        # Process all races in the season
        races_dir = F1DB_DATA_DIR / 'seasons' / season / 'races'
        if races_dir.exists():
            race_dirs = sorted([d for d in races_dir.iterdir() if d.is_dir()])
            for race_dir in race_dirs:
                race_results_file = race_dir / 'race-results.yml'
                qualifying_results_file = race_dir / 'qualifying-results.yml'
                
                # Process race results for wins
                if race_results_file.exists():
                    race_results = load_yaml(race_results_file)
                    if race_results and isinstance(race_results, list):
                        race_count += 1
                        for result in race_results:
                            driver_id = result.get('driverId')
                            if driver_id:
                                driver_stats[driver_id]['totalRaces'] += 1
                                # Check if this is a win (position 1)
                                if result.get('position') == 1 or result.get('positionNumber') == 1:
                                    driver_stats[driver_id]['totalWins'] += 1
                
                # Process qualifying results for poles
                if qualifying_results_file.exists():
                    qualifying_results = load_yaml(qualifying_results_file)
                    if qualifying_results and isinstance(qualifying_results, list) and len(qualifying_results) > 0:
                        pole_driver = qualifying_results[0]
                        driver_id = pole_driver.get('driverId')
                        if driver_id:
                            driver_stats[driver_id]['totalPoles'] += 1
    
    print(f"   Processed {race_count} races")
    
    # Count championships per driver
    for driver_id, season in championship_winners:
        driver_stats[driver_id]['totalChampionships'] += 1
    
    return driver_stats

def generate_driver_stats():
    """Generate aggregated driver statistics."""
    drivers_dir = F1DB_DATA_DIR / 'drivers'
    if not drivers_dir.exists():
        return []
    
    # Load driver info
    driver_info = {}
    driver_files = list(drivers_dir.glob('*.yml'))
    
    print(f"   Loading info for {len(driver_files)} drivers...")
    for driver_file in driver_files:
        data = load_yaml(driver_file)
        if data and data.get('id'):
            driver_info[data['id']] = {
                'id': data.get('id'),
                'firstName': data.get('firstName', ''),
                'lastName': data.get('lastName', ''),
                'countryId': data.get('nationalityCountryId') or data.get('countryId'),
            }
    
    # Calculate stats from races
    stats_from_races = calculate_driver_stats_from_races()
    
    # Combine driver info with calculated stats
    driver_stats = []
    for driver_id, info in driver_info.items():
        stats = stats_from_races.get(driver_id, {
            'totalRaces': 0,
            'totalWins': 0,
            'totalPoles': 0,
            'totalChampionships': 0
        })
        driver_stats.append({
            **info,
            **stats
        })
    
    # Sort by wins descending
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

def json_serial(obj):
    """JSON serializer for objects not serializable by default json code"""
    if isinstance(obj, (datetime, date)):
        return obj.isoformat()
    raise TypeError(f"Type {type(obj)} not serializable")

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
        json.dump(driver_championships, f, indent=2, default=json_serial)
    print(f"   ✓ Generated {len(driver_championships)} records")

    print("\n2. Generating constructor championships history...")
    constructor_championships = generate_constructor_standings_history()
    with open(OUTPUT_DIR / 'constructor-championships.json', 'w') as f:
        json.dump(constructor_championships, f, indent=2, default=json_serial)
    print(f"   ✓ Generated {len(constructor_championships)} records")

    print("\n3. Generating current season data...")
    current_season = generate_current_season_data()
    if current_season:
        with open(OUTPUT_DIR / 'current-season.json', 'w') as f:
            json.dump(current_season, f, indent=2, default=json_serial)
        print(f"   ✓ Generated data for season {current_season['season']}")
        print(f"   ✓ Driver standings: {len(current_season['driverStandings'])} drivers")
        print(f"   ✓ Constructor standings: {len(current_season['constructorStandings'])} constructors")
        print(f"   ✓ Total races: {current_season.get('totalRaces', 0)}")
        print(f"   ✓ Unique drivers: {current_season.get('uniqueDrivers', 0)}")
        print(f"   ✓ Recent races: {len(current_season['recentRaces'])} races")

    print("\n4. Generating driver statistics...")
    driver_stats = generate_driver_stats()
    with open(OUTPUT_DIR / 'driver-stats.json', 'w') as f:
        json.dump(driver_stats, f, indent=2, default=json_serial)
    print(f"   ✓ Generated {len(driver_stats)} driver records")
    top_5 = driver_stats[:5]
    print(f"   Top 5 drivers by wins:")
    for i, driver in enumerate(top_5, 1):
        print(f"     {i}. {driver.get('firstName', '')} {driver.get('lastName', '')}: {driver['totalWins']} wins, {driver['totalPoles']} poles, {driver['totalChampionships']} championships")

    print("\n5. Generating circuits data...")
    circuits = generate_circuits_data()
    with open(OUTPUT_DIR / 'circuits.json', 'w') as f:
        json.dump(circuits, f, indent=2, default=json_serial)
    print(f"   ✓ Generated {len(circuits)} circuit records")

    print("\n" + "=" * 50)
    print("✓ Data generation complete!")
    print(f"  Output directory: {OUTPUT_DIR}")

if __name__ == '__main__':
    main()
