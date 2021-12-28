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
( ?1.year
, ?2.entrantId
, ?3.constructorId
, ?3.engineManufacturerId
, ?4.driverId
, ?5.rounds
, ?4.roundsText
, ?4.testDriver
);
