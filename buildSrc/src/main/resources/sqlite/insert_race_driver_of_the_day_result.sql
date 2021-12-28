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
, ?3.percentage
);
