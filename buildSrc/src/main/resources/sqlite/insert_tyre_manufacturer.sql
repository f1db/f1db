INSERT INTO tyre_manufacturer
( id
, name
, country_id
, best_race_result
, best_starting_grid_position
, total_race_entries
, total_race_starts
, total_race_wins
, total_race_laps
, total_podiums
, total_podium_races
, total_pole_positions
, total_fastest_laps
)
VALUES
( :id
, :name
, :countryId
, :bestRaceResult
, :bestStartingGridPosition
, :totalRaceEntries
, :totalRaceStarts
, :totalRaceWins
, :totalRaceLaps
, :totalPodiums
, :totalPodiumRaces
, :totalPolePositions
, :totalFastestLaps
);
