INSERT INTO driver_standing
( year
, round
, position_display_order
, position_number
, position_text
, driver_id
, points
)
VALUES
( ?1.year
, ?2.round
, ?2.positionDisplayOrder
, ?3.positionNumber
, ?3.positionText
, ?3.driverId
, ?3.points
);
