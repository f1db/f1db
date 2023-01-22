INSERT INTO constructor
( id
, name
, full_name
, country_id
, best_championship_position
, best_race_result
, best_starting_grid_position
, total_championship_wins
, total_race_entries
, total_race_starts
, total_race_wins
, total_1_and_2_finishes
, total_race_laps
, total_podiums
, total_podium_races
, total_championship_points
, total_pole_positions
, total_fastest_laps
)
VALUES
( :id
, :name
, :fullName
, :countryId
, :bestChampionshipPosition
, :bestRaceResult
, :bestStartingGridPosition
, :totalChampionshipWins
, :totalRaceEntries
, :totalRaceStarts
, :totalRaceWins
, :total1And2Finishes
, :totalRaceLaps
, :totalPodiums
, :totalPodiumRaces
, :totalChampionshipPoints
, :totalPolePositions
, :totalFastestLaps
);
