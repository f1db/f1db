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
, total_races_held
)
VALUES
( :id
, :name
, :fullName
, :previousNamesAsString
, :type
, :placeName
, :countryId
, :latitude
, :longitude
, :totalRacesHeld
);
