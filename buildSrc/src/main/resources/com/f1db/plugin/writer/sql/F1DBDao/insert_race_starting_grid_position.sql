INSERT INTO race_data
( race_id
, type
, position_display_order
, position_number
, position_text
, driver_number
, driver_id
, constructor_id
, engine_manufacturer_id
, tyre_manufacturer_id
, starting_grid_position_grid_penalty
, starting_grid_position_grid_penalty_positions
, starting_grid_position_time
, starting_grid_position_time_millis
)
VALUES
( :raceId
, :raceDataType
, :positionDisplayOrder
, :positionNumber
, :positionText
, :driverNumber
, :driverId
, :constructorId
, :engineManufacturerId
, :tyreManufacturerId
, :gridPenalty
, :gridPenaltyPositions
, :time
, :timeMillis
);
