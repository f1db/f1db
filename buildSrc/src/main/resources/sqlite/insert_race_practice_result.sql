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
( ?1.id
, ?2.type
, ?2.positionDisplayOrder
, ?3.positionNumber
, ?3.positionText
, ?3.driverNumber
, ?3.driverId
, ?3.constructorId
, ?3.engineManufacturerId
, ?3.tyreManufacturerId
, ?3.time
, ?3.timeMillis
, ?3.gap
, ?3.gapMillis
, ?3.interval
, ?3.intervalMillis
, ?3.laps
);
