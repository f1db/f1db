INSERT INTO race_constructor_standing
( race_id
, position_display_order
, position_number
, position_text
, constructor_id
, engine_manufacturer_id
, points
, positions_gained
)
VALUES
( :raceId
, :positionDisplayOrder
, :positionNumber
, :positionText
, :constructorId
, :engineManufacturerId
, :points
, :positionsGained
);
