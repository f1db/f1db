INSERT INTO race_driver_standing
( race_id
, position_display_order
, position_number
, position_text
, driver_id
, points
, positions_gained
)
VALUES
( :raceId
, :positionDisplayOrder
, :positionNumber
, :positionText
, :driverId
, :points
, :positionsGained
);
