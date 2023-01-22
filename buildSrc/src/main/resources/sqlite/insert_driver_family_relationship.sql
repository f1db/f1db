INSERT INTO driver_family_relationship
( driver_id
, other_driver_id
, type
)
VALUES
( :parentDriverId
, :driverId
, :type
);
