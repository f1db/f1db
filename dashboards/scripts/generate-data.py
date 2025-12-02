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
    
    # Statistics for current season
    podium_stats = defaultdict(lambda: {'1st': 0, '2nd': 0, '3rd': 0, 'podiums': []})
    dnf_stats = defaultdict(lambda: {'count': 0, 'races': []})
    time_penalty_stats = defaultdict(lambda: {'count': 0, 'total_seconds': 0})
    grid_penalty_stats = defaultdict(lambda: {'count': 0, 'total_places': 0})
    
    if races_dir.exists():
        race_dirs = sorted([d for d in races_dir.iterdir() if d.is_dir()])
        for race_dir in race_dirs:
            race_file = race_dir / 'race.yml'
            race_results_file = race_dir / 'race-results.yml'
            starting_grid_file = race_dir / 'starting-grid-positions.yml'

            if race_file.exists() and race_results_file.exists():
                race_info = load_yaml(race_file)
                race_results = load_yaml(race_results_file)
                starting_grid = load_yaml(starting_grid_file) if starting_grid_file.exists() else None

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
                    
                    # Track unique drivers and calculate statistics
                    if race_results and isinstance(race_results, list):
                        for result in race_results:
                            driver_id = result.get('driverId')
                            if driver_id:
                                unique_drivers.add(driver_id)
                                
                                # Podium positions (1st, 2nd, 3rd) - track with race and circuit info
                                position = result.get('position') or result.get('positionNumber')
                                if position in [1, 2, 3]:
                                    race_name = race_info.get('officialName') or race_info.get('grandPrixId', 'Unknown')
                                    circuit_id = race_info.get('circuitId', 'Unknown')
                                    
                                    # Load circuit name
                                    circuit_name = circuit_id
                                    circuit_file = F1DB_DATA_DIR / 'circuits' / f'{circuit_id}.yml'
                                    if circuit_file.exists():
                                        circuit_data = load_yaml(circuit_file)
                                        if circuit_data and circuit_data.get('name'):
                                            circuit_name = circuit_data.get('name')
                                    
                                    # Track podium with race and circuit
                                    podium_stats[driver_id]['podiums'].append({
                                        'position': position,
                                        'race': race_name,
                                        'circuit': circuit_name
                                    })
                                    
                                    if position == 1:
                                        podium_stats[driver_id]['1st'] += 1
                                    elif position == 2:
                                        podium_stats[driver_id]['2nd'] += 1
                                    elif position == 3:
                                        podium_stats[driver_id]['3rd'] += 1
                                
                                # DNF (Did Not Finish) - check both position and positionText
                                position_value = result.get('position')
                                position_text = result.get('positionText', '')
                                if (isinstance(position_value, str) and position_value.upper() == 'DNF') or \
                                   (isinstance(position_text, str) and position_text.upper() == 'DNF'):
                                    dnf_stats[driver_id]['count'] += 1
                                    # Track which race they DNF'd in - use official name and circuit
                                    race_name = race_info.get('officialName') or race_info.get('grandPrixId', 'Unknown')
                                    circuit_id = race_info.get('circuitId', 'Unknown')
                                    
                                    # Load circuit name
                                    circuit_name = circuit_id
                                    circuit_file = F1DB_DATA_DIR / 'circuits' / f'{circuit_id}.yml'
                                    if circuit_file.exists():
                                        circuit_data = load_yaml(circuit_file)
                                        if circuit_data and circuit_data.get('name'):
                                            circuit_name = circuit_data.get('name')
                                    
                                    # Store as object with race and circuit
                                    race_entry = {
                                        'race': race_name,
                                        'circuit': circuit_name
                                    }
                                    # Check if this race is already in the list (by race name)
                                    if not any(r.get('race') == race_name for r in dnf_stats[driver_id]['races']):
                                        dnf_stats[driver_id]['races'].append(race_entry)
                                
                                # Time penalties - track individual penalties with race info for tooltips
                                time_penalty = result.get('timePenalty')
                                race_name = race_info.get('officialName') or race_info.get('grandPrixId', 'Unknown')
                                circuit_id = race_info.get('circuitId', 'Unknown')
                                
                                # Load circuit name
                                circuit_name = circuit_id
                                circuit_file = F1DB_DATA_DIR / 'circuits' / f'{circuit_id}.yml'
                                if circuit_file.exists():
                                    circuit_data = load_yaml(circuit_file)
                                    if circuit_data and circuit_data.get('name'):
                                        circuit_name = circuit_data.get('name')
                                
                                if time_penalty:
                                    time_penalty_stats[driver_id]['count'] += 1
                                    # Parse time penalty (format: "+5s", "+10s", etc.)
                                    try:
                                        seconds = float(str(time_penalty).replace('+', '').replace('s', '').replace(' ', ''))
                                        time_penalty_stats[driver_id]['total_seconds'] += seconds
                                    except:
                                        pass
                                    # Store penalty details with full race name and circuit
                                    if 'penalties' not in time_penalty_stats[driver_id]:
                                        time_penalty_stats[driver_id]['penalties'] = []
                                    time_penalty_stats[driver_id]['penalties'].append({
                                        'amount': str(time_penalty),
                                        'race': race_name,
                                        'circuit': circuit_name
                                    })
                                # Also check timePenaltyMillis if available
                                time_penalty_millis = result.get('timePenaltyMillis')
                                if time_penalty_millis:
                                    if time_penalty_stats[driver_id]['count'] == 0:
                                        time_penalty_stats[driver_id]['count'] += 1
                                        time_penalty_stats[driver_id]['penalties'] = []
                                    time_penalty_stats[driver_id]['total_seconds'] += time_penalty_millis / 1000.0
                                    penalty_str = f"+{time_penalty_millis / 1000.0:.1f}s"
                                    time_penalty_stats[driver_id]['penalties'].append({
                                        'amount': penalty_str,
                                        'race': race_name,
                                        'circuit': circuit_name
                                    })
                    
                    # Grid penalties from starting grid - track with race info
                    race_name = race_info.get('officialName') or race_info.get('grandPrixId', 'Unknown')
                    circuit_id = race_info.get('circuitId', 'Unknown')
                    
                    # Load circuit name
                    circuit_name = circuit_id
                    circuit_file = F1DB_DATA_DIR / 'circuits' / f'{circuit_id}.yml'
                    if circuit_file.exists():
                        circuit_data = load_yaml(circuit_file)
                        if circuit_data and circuit_data.get('name'):
                            circuit_name = circuit_data.get('name')
                    
                    if starting_grid and isinstance(starting_grid, list):
                        for grid_pos in starting_grid:
                            driver_id = grid_pos.get('driverId')
                            grid_penalty = grid_pos.get('gridPenalty')
                            if driver_id and grid_penalty:
                                grid_penalty_stats[driver_id]['count'] += 1
                                # Parse grid penalty (format: "-3", "-5", etc. or number of places)
                                places = None
                                try:
                                    places = int(str(grid_penalty).replace('-', '').replace('+', ''))
                                    grid_penalty_stats[driver_id]['total_places'] += places
                                except:
                                    try:
                                        places = abs(int(grid_penalty))
                                        grid_penalty_stats[driver_id]['total_places'] += places
                                    except:
                                        pass
                                
                                # Store penalty details with full race name and circuit
                                if 'penalties' not in grid_penalty_stats[driver_id]:
                                    grid_penalty_stats[driver_id]['penalties'] = []
                                
                                penalty_amount = f"{places} places" if places is not None else str(grid_penalty)
                                grid_penalty_stats[driver_id]['penalties'].append({
                                    'amount': penalty_amount,
                                    'race': race_name,
                                    'circuit': circuit_name
                                })
        
        # Get last 5 races for recent races
        recent_races = all_races[-5:]

    # Convert podium stats to list format
    podium_list = []
    for driver_id, positions in podium_stats.items():
        total_podiums = positions['1st'] + positions['2nd'] + positions['3rd']
        if total_podiums > 0:
            # Sort podiums chronologically (first to latest) - they're already in order from race processing
            podiums = positions.get('podiums', [])
            podium_list.append({
                'driverId': driver_id,
                '1st': positions['1st'],
                '2nd': positions['2nd'],
                '3rd': positions['3rd'],
                'total': total_podiums,
                'podiums': podiums  # List of podiums with position, race, circuit
            })
    podium_list.sort(key=lambda x: x['total'], reverse=True)
    
    # Convert DNF stats to list (races are already objects with race and circuit)
    dnf_list = []
    for driver_id, stats in dnf_stats.items():
        if stats['count'] > 0:
            dnf_list.append({
                'driverId': driver_id,
                'count': stats['count'],
                'races': stats['races']  # Already a list of objects with race and circuit
            })
    dnf_list.sort(key=lambda x: x['count'], reverse=True)
    
    # Convert time penalty stats to list - sort by total seconds
    time_penalty_list = []
    for driver_id, stats in time_penalty_stats.items():
        if stats['count'] > 0:
            time_penalty_list.append({
                'driverId': driver_id,
                'count': stats['count'],
                'totalSeconds': round(stats['total_seconds'], 2),
                'penalties': stats.get('penalties', [])
            })
    time_penalty_list.sort(key=lambda x: x['totalSeconds'], reverse=True)
    
    # Convert grid penalty stats to list - sort by total places
    grid_penalty_list = []
    for driver_id, stats in grid_penalty_stats.items():
        if stats['count'] > 0:
            grid_penalty_list.append({
                'driverId': driver_id,
                'count': stats['count'],
                'totalPlaces': stats['total_places'],
                'penalties': stats.get('penalties', [])
            })
    grid_penalty_list.sort(key=lambda x: x['totalPlaces'], reverse=True)

    return {
        'season': int(current_season),
        'driverStandings': driver_standings,
        'constructorStandings': constructor_standings,
        'totalRaces': len(all_races),
        'uniqueDrivers': len(unique_drivers),
        'recentRaces': recent_races,
        'podiumStats': podium_list[:10],  # Top 10
        'dnfStats': dnf_list[:10],  # Top 10
        'timePenaltyStats': time_penalty_list[:10],  # Top 10
        'gridPenaltyStats': grid_penalty_list[:10]  # Top 10
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

def calculate_dgi_metrics():
    """
    Calculate Driver Greatness Index (DGI) metrics for all drivers.
    Returns a dictionary with all DGI components for each driver.
    """
    seasons = get_all_seasons()
    
    # Initialize metrics for all drivers
    driver_metrics = defaultdict(lambda: {
        'totalRaces': 0,
        'podiums': 0,
        'wins': 0,
        'poles': 0,
        'nonPoleWins': 0,
        'nonPolePoints': 0,  # Sum of grid positions for non-pole wins (weighted)
        'championships': 0,
        'teammateComparisons': [],  # List of (race_year_round, constructorId, finishedAhead)
        'seasons': set(),
        'constructors': set(),
        'firstSeason': None,
        'lastSeason': None
    })
    
    # Track championship winners
    championship_winners = {}
    for season in seasons:
        standings_file = F1DB_DATA_DIR / 'seasons' / season / 'driver-standings.yml'
        if standings_file.exists():
            data = load_yaml(standings_file)
            if data and isinstance(data, list) and len(data) > 0:
                champion_id = data[0].get('driverId')
                if champion_id:
                    championship_winners[champion_id] = championship_winners.get(champion_id, 0) + 1
    
    print("   Processing races for DGI calculation...")
    race_count = 0
    
    # Process each race to gather all metrics
    for season in seasons:
        races_dir = F1DB_DATA_DIR / 'seasons' / season / 'races'
        if not races_dir.exists():
            continue
            
        race_dirs = sorted([d for d in races_dir.iterdir() if d.is_dir()])
        for race_dir in race_dirs:
            race_results_file = race_dir / 'race-results.yml'
            qualifying_results_file = race_dir / 'qualifying-results.yml'
            
            if not race_results_file.exists():
                continue
                
            race_results = load_yaml(race_results_file)
            if not race_results or not isinstance(race_results, list):
                continue
                
            race_count += 1
            season_int = int(season)
            race_name = race_dir.name
            
            # Store race results by constructor for teammate comparison
            constructor_results = defaultdict(list)
            
            # Process race results
            for result in race_results:
                driver_id = result.get('driverId')
                if not driver_id:
                    continue
                    
                constructor_id = result.get('constructorId')
                position = result.get('position') or result.get('positionNumber')
                grid_position = result.get('gridPosition')
                
                if not position or not isinstance(position, int):
                    continue
                
                # Update basic metrics
                driver_metrics[driver_id]['totalRaces'] += 1
                driver_metrics[driver_id]['seasons'].add(season_int)
                if constructor_id:
                    driver_metrics[driver_id]['constructors'].add(constructor_id)
                
                # Podium (position 1-3)
                if position <= 3:
                    driver_metrics[driver_id]['podiums'] += 1
                
                # Win
                if position == 1:
                    driver_metrics[driver_id]['wins'] += 1
                    
                    # Win from non-pole (grid position > 1)
                    if grid_position and isinstance(grid_position, int) and grid_position > 1:
                        driver_metrics[driver_id]['nonPoleWins'] += 1
                        # Weight: points based on starting position (P2 = +1, P3 = +2, etc.)
                        driver_metrics[driver_id]['nonPolePoints'] += grid_position - 1
                
                # Store for teammate comparison
                if constructor_id:
                    constructor_results[constructor_id].append({
                        'driverId': driver_id,
                        'position': position
                    })
            
            # Process teammate comparisons
            for constructor_id, results in constructor_results.items():
                if len(results) >= 2:  # Need at least 2 drivers from same constructor
                    # Track all drivers from this constructor in this race
                    # We need to record ALL races where drivers had teammates, not just when they finished ahead
                    for result in results:
                        driver_id = result['driverId']
                        driver_position = result['position']
                        
                        # Count how many teammates finished ahead (better position = lower number)
                        teammates_ahead = sum(1 for r in results if r['driverId'] != driver_id and r['position'] < driver_position)
                        # Count how many teammates finished behind (worse position = higher number)
                        teammates_behind = sum(1 for r in results if r['driverId'] != driver_id and r['position'] > driver_position)
                        # Total teammates in this race
                        total_teammates = len(results) - 1
                        
                        # Record ALL races with teammates (even if finished behind all of them)
                        # This ensures we have accurate denominator for percentage calculation
                        driver_metrics[driver_id]['teammateComparisons'].append({
                            'race': f"{season_int}-{race_name}",
                            'constructorId': constructor_id,
                            'finishedAhead': teammates_behind,
                            'finishedBehind': teammates_ahead,
                            'totalTeammates': total_teammates,
                            'position': driver_position
                        })
            
            # Process qualifying for pole positions
            if qualifying_results_file.exists():
                qualifying_results = load_yaml(qualifying_results_file)
                if qualifying_results and isinstance(qualifying_results, list) and len(qualifying_results) > 0:
                    pole_driver = qualifying_results[0]
                    driver_id = pole_driver.get('driverId')
                    if driver_id:
                        driver_metrics[driver_id]['poles'] += 1
    
    print(f"   Processed {race_count} races")
    
    # Load driver info
    drivers_dir = F1DB_DATA_DIR / 'drivers'
    driver_info = {}
    if drivers_dir.exists():
        driver_files = list(drivers_dir.glob('*.yml'))
        for driver_file in driver_files:
            data = load_yaml(driver_file)
            if data and data.get('id'):
                driver_info[data['id']] = {
                    'id': data.get('id'),
                    'firstName': data.get('firstName', ''),
                    'lastName': data.get('lastName', ''),
                    'countryId': data.get('nationalityCountryId') or data.get('countryId'),
                }
    
    # Build final DGI data
    dgi_data = []
    
    for driver_id, metrics in driver_metrics.items():
        if driver_id not in driver_info:
            continue
        
        # Calculate teammate dominance percentage
        teammate_comparisons = metrics['teammateComparisons']
        if teammate_comparisons:
            # Count races where driver finished ahead of at least one teammate
            races_ahead = sum(1 for comp in teammate_comparisons if comp.get('finishedAhead', 0) > 0)
            # Total races with teammates (should now include all races, not just when ahead)
            total_races_with_teammate = len(teammate_comparisons)
            # Percentage of races where driver finished ahead of at least one teammate
            # This gives us a realistic percentage (0-100%) where 100% would mean never finishing behind a teammate
            teammate_dominance = (races_ahead / total_races_with_teammate) * 100 if total_races_with_teammate > 0 else 0
        else:
            teammate_dominance = 0
            total_races_with_teammate = 0
        
        # Calculate podium percentage
        podium_percentage = (metrics['podiums'] / metrics['totalRaces'] * 100) if metrics['totalRaces'] > 0 else 0
        
        # Career span
        seasons_list = sorted(list(metrics['seasons']))
        career_length = len(seasons_list)
        first_season = min(seasons_list) if seasons_list else None
        last_season = max(seasons_list) if seasons_list else None
        
        # Number of constructors
        num_constructors = len(metrics['constructors'])
        
        # Championships
        championships = championship_winners.get(driver_id, 0)
        
        dgi_data.append({
            **driver_info[driver_id],
            # Raw metrics
            'totalRaces': metrics['totalRaces'],
            'podiums': metrics['podiums'],
            'wins': metrics['wins'],
            'poles': metrics['poles'],
            'nonPoleWins': metrics['nonPoleWins'],
            'nonPolePoints': metrics['nonPolePoints'],
            'championships': championships,
            # Calculated percentages
            'teammateDominance': round(teammate_dominance, 2),
            'teammateRaces': total_races_with_teammate,
            'podiumPercentage': round(podium_percentage, 2),
            # Longevity
            'careerLength': career_length,
            'numConstructors': num_constructors,
            'firstSeason': first_season,
            'lastSeason': last_season,
        })
    
    return dgi_data

def calculate_normalized_dgi(dgi_data):
    """
    Normalize DGI metrics and calculate weighted Driver Greatness Index.
    """
    if not dgi_data:
        return []
    
    # Extract metrics for normalization
    metrics_df = []
    for driver in dgi_data:
        metrics_df.append({
            'driverId': driver['id'],
            'teammateDominance': driver['teammateDominance'],
            'podiumPercentage': driver['podiumPercentage'],
            'nonPolePoints': driver['nonPolePoints'],
            'poles': driver['poles'],
            'championships': driver['championships'],
            'careerLength': driver['careerLength'],
            'numConstructors': driver['numConstructors'],
        })
    
    # Find min/max for normalization
    max_values = {
        'teammateDominance': max(m['teammateDominance'] for m in metrics_df) if metrics_df else 100,
        'podiumPercentage': max(m['podiumPercentage'] for m in metrics_df) if metrics_df else 100,
        'nonPolePoints': max(m['nonPolePoints'] for m in metrics_df) if metrics_df else 1,
        'poles': max(m['poles'] for m in metrics_df) if metrics_df else 1,
        'championships': max(m['championships'] for m in metrics_df) if metrics_df else 1,
        'careerLength': max(m['careerLength'] for m in metrics_df) if metrics_df else 1,
        'numConstructors': max(m['numConstructors'] for m in metrics_df) if metrics_df else 1,
    }
    
    min_values = {
        'teammateDominance': min(m['teammateDominance'] for m in metrics_df) if metrics_df else 0,
        'podiumPercentage': min(m['podiumPercentage'] for m in metrics_df) if metrics_df else 0,
        'nonPolePoints': min(m['nonPolePoints'] for m in metrics_df) if metrics_df else 0,
        'poles': min(m['poles'] for m in metrics_df) if metrics_df else 0,
        'championships': min(m['championships'] for m in metrics_df) if metrics_df else 0,
        'careerLength': min(m['careerLength'] for m in metrics_df) if metrics_df else 0,
        'numConstructors': min(m['numConstructors'] for m in metrics_df) if metrics_df else 0,
    }
    
    # Weights from notebook
    weights = {
        'teammateDominance': 0.25,
        'podiumPercentage': 0.20,
        'nonPolePoints': 0.20,
        'poles': 0.15,
        'championships': 0.10,
        'careerLength': 0.05,
        'numConstructors': 0.05,
    }
    
    # Calculate normalized DGI for each driver
    for driver in dgi_data:
        # Normalize each metric (0-1 scale)
        normalized = {}
        for metric in ['teammateDominance', 'podiumPercentage', 'nonPolePoints', 'poles', 'championships', 'careerLength', 'numConstructors']:
            value = driver.get(metric, 0)
            min_val = min_values[metric]
            max_val = max_values[metric]
            range_val = max_val - min_val
            
            if range_val > 0:
                normalized[metric] = (value - min_val) / range_val
            else:
                normalized[metric] = 0.0
        
        # Calculate weighted DGI
        dgi_score = sum(normalized[metric] * weights[metric] for metric in weights.keys())
        
        driver['normalizedMetrics'] = normalized
        driver['dgi'] = round(dgi_score, 4)
    
    # Sort by DGI descending
    dgi_data.sort(key=lambda x: x['dgi'], reverse=True)
    
    return dgi_data

def generate_driver_greatness_index():
    """Generate Driver Greatness Index data."""
    print("   Calculating DGI metrics...")
    dgi_data = calculate_dgi_metrics()
    
    print("   Normalizing and calculating DGI scores...")
    dgi_data = calculate_normalized_dgi(dgi_data)
    
    return dgi_data

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

    print("\n6. Generating Driver Greatness Index (DGI)...")
    dgi_data = generate_driver_greatness_index()
    with open(OUTPUT_DIR / 'driver-greatness-index.json', 'w') as f:
        json.dump(dgi_data, f, indent=2, default=json_serial)
    print(f"   ✓ Generated {len(dgi_data)} driver DGI records")
    if dgi_data:
        top_5_dgi = dgi_data[:5]
        print(f"   Top 5 drivers by DGI:")
        for i, driver in enumerate(top_5_dgi, 1):
            name = f"{driver.get('firstName', '')} {driver.get('lastName', '')}".strip()
            print(f"     {i}. {name}: DGI {driver['dgi']:.4f} (Wins: {driver['wins']}, Championships: {driver['championships']}, Teammate Dominance: {driver['teammateDominance']:.1f}%)")

    print("\n" + "=" * 50)
    print("✓ Data generation complete!")
    print(f"  Output directory: {OUTPUT_DIR}")

if __name__ == '__main__':
    main()
