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
, pit_stop_stop
, pit_stop_lap
, pit_stop_time
, pit_stop_time_millis
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
, :stop
, :lap
, :time
, :timeMillis
);
