INSERT INTO constructor_standing
( year
, round
, position_display_order
, position_number
, position_text
, constructor_id
, engine_manufacturer_id
, points
)
VALUES
( ?1.year
, ?2.round
, ?2.positionDisplayOrder
, ?3.positionNumber
, ?3.positionText
, ?3.constructorId
, ?3.engineManufacturerId
, ?3.points
);
