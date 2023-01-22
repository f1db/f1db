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
, practice_time
, practice_time_millis
, practice_gap
, practice_gap_millis
, practice_interval
, practice_interval_millis
, practice_laps
)
VALUES
( :raceId
, :type
, :positionDisplayOrder
, :positionNumber
, :positionText
, :driverNumber
, :driverId
, :constructorId
, :engineManufacturerId
, :tyreManufacturerId
, :time
, :timeMillis
, :gap
, :gapMillis
, :interval
, :intervalMillis
, :laps
);
