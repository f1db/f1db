INSERT INTO circuit
( id
, name
, full_name
, previous_names
, type
, place_name
, country_id
, latitude
, longitude
)
VALUES
( ?.id
, ?.name
, ?.fullName
, ?2.previousNames
, ?.type
, ?.placeName
, ?.countryId
, ?.latitude
, ?.longitude
);
