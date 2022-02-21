INSERT INTO driver_family_relationship
( driver_id
, other_driver_id
, type
)
VALUES
( ?1.id
, ?2.driverId
, ?2.type
);
