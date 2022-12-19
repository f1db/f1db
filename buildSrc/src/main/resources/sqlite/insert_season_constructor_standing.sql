INSERT INTO season_constructor_standing
( year
, position_display_order
, position_number
, position_text
, constructor_id
, engine_manufacturer_id
, points
)
VALUES
( ?1.year
, ?2.positionDisplayOrder
, ?3.positionNumber
, ?3.positionText
, ?3.constructorId
, ?3.engineManufacturerId
, ?3.points
);
