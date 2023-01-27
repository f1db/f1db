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
, driver_of_the_day_percentage
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
, :percentage
);
