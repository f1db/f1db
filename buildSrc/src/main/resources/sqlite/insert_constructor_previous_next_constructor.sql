INSERT INTO constructor_previous_next_constructor
( constructor_id
, previous_next_constructor_id
, year_from
, year_to
)
VALUES
( ?1.id
, ?2.constructorId
, ?2.yearFrom
, ?2.yearTo
);
