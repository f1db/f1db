INSERT INTO season_entrant_driver
( year
, entrant_id
, constructor_id
, engine_manufacturer_id
, driver_id
, rounds
, rounds_text
, test_driver
)
VALUES
( :year
, :entrantId
, :constructorId
, :engineManufacturerId
, :driverId
, :rounds
, :roundsText
, :testDriver
);
