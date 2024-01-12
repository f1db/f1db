INSERT INTO race
( id
, year
, round
, date
, grand_prix_id
, official_name
, qualifying_format
, sprint_qualifying_format
, circuit_id
, circuit_type
, course_length
, laps
, distance
, scheduled_laps
, scheduled_distance
)
VALUES
( :id
, :year
, :round
, :date
, :grandPrixId
, :officialName
, :qualifyingFormat
, :sprintQualifyingFormat
, :circuitId
, :circuitType
, :courseLength
, :laps
, :distance
, :scheduledLaps
, :scheduledDistance
);
