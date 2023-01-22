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
, fastest_lap_lap
, fastest_lap_time
, fastest_lap_time_millis
, fastest_lap_gap
, fastest_lap_gap_millis
, fastest_lap_interval
, fastest_lap_interval_millis
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
, :lap
, :time
, :timeMillis
, :gap
, :gapMillis
, :interval
, :intervalMillis
);
