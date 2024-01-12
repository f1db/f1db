CREATE TABLE continent
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, code VARCHAR(2) NOT NULL COLLATE NOCASE UNIQUE
, name VARCHAR(255) NOT NULL COLLATE NOCASE UNIQUE
, demonym VARCHAR(255) COLLATE NOCASE
);

CREATE TABLE country
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, alpha2_code VARCHAR(2) NOT NULL COLLATE NOCASE UNIQUE
, alpha3_code VARCHAR(3) NOT NULL COLLATE NOCASE UNIQUE
, name VARCHAR(255) NOT NULL COLLATE NOCASE UNIQUE
, demonym VARCHAR(255) COLLATE NOCASE
, continent_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES continent(id)
);

CREATE INDEX country_continent_id_index ON country(continent_id COLLATE NOCASE);

CREATE TABLE driver
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
, first_name VARCHAR(255) NOT NULL COLLATE NOCASE
, last_name VARCHAR(255) NOT NULL COLLATE NOCASE
, full_name VARCHAR(255) NOT NULL COLLATE NOCASE
, abbreviation VARCHAR(3) NOT NULL COLLATE NOCASE
, permanent_number VARCHAR(2) COLLATE NOCASE
, gender VARCHAR(255) NOT NULL COLLATE NOCASE
, date_of_birth DATE NOT NULL
, date_of_death DATE
, place_of_birth VARCHAR(255) NOT NULL COLLATE NOCASE
, country_of_birth_country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, nationality_country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, second_nationality_country_id VARCHAR(255) COLLATE NOCASE REFERENCES country(id)
, best_championship_position INTEGER
, best_race_result INTEGER
, best_starting_grid_position INTEGER
, total_championship_wins INTEGER NOT NULL
, total_race_entries INTEGER NOT NULL
, total_race_starts INTEGER NOT NULL
, total_race_wins INTEGER NOT NULL
, total_race_laps INTEGER NOT NULL
, total_podiums INTEGER NOT NULL
, total_points NUMERIC NOT NULL
, total_championship_points NUMERIC NOT NULL
, total_pole_positions INTEGER NOT NULL
, total_fastest_laps INTEGER NOT NULL
, total_driver_of_the_day INTEGER NOT NULL
, total_grand_slams INTEGER NOT NULL
);

CREATE INDEX driver_name_index ON driver(name COLLATE NOCASE);
CREATE INDEX driver_first_name_index ON driver(first_name COLLATE NOCASE);
CREATE INDEX driver_last_name_index ON driver(last_name COLLATE NOCASE);
CREATE INDEX driver_full_name_index ON driver(full_name COLLATE NOCASE);
CREATE INDEX driver_abbreviation_index ON driver(abbreviation COLLATE NOCASE);
CREATE INDEX driver_permanent_number_index ON driver(permanent_number COLLATE NOCASE);
CREATE INDEX driver_gender_index ON driver(gender COLLATE NOCASE);
CREATE INDEX driver_date_of_birth_index ON driver(date_of_birth);
CREATE INDEX driver_date_of_death_index ON driver(date_of_death);
CREATE INDEX driver_place_of_birth_index ON driver(place_of_birth COLLATE NOCASE);
CREATE INDEX driver_country_of_birth_country_id_index ON driver(country_of_birth_country_id COLLATE NOCASE);
CREATE INDEX driver_nationality_country_id_index ON driver(nationality_country_id COLLATE NOCASE);
CREATE INDEX driver_second_nationality_country_id_index ON driver(second_nationality_country_id COLLATE NOCASE);

CREATE TABLE driver_family_relationship
( driver_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES driver(id)
, other_driver_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES driver(id)
, type VARCHAR(255) NOT NULL COLLATE NOCASE
, PRIMARY KEY (driver_id, other_driver_id, type)
);

CREATE INDEX driver_family_relationship_driver_id_index ON driver_family_relationship(driver_id COLLATE NOCASE);
CREATE INDEX driver_family_relationship_other_driver_id_index ON driver_family_relationship(other_driver_id COLLATE NOCASE);

CREATE TABLE constructor
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
, full_name VARCHAR(255) NOT NULL COLLATE NOCASE
, country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, best_championship_position INTEGER
, best_race_result INTEGER
, best_starting_grid_position INTEGER
, total_championship_wins INTEGER NOT NULL
, total_race_entries INTEGER NOT NULL
, total_race_starts INTEGER NOT NULL
, total_race_wins INTEGER NOT NULL
, total_1_and_2_finishes INTEGER NOT NULL
, total_race_laps INTEGER NOT NULL
, total_podiums INTEGER NOT NULL
, total_podium_races INTEGER NOT NULL
, total_championship_points NUMERIC NOT NULL
, total_pole_positions INTEGER NOT NULL
, total_fastest_laps INTEGER NOT NULL
);

CREATE INDEX constructor_name_index ON constructor(name COLLATE NOCASE);
CREATE INDEX constructor_full_name_index ON constructor(full_name COLLATE NOCASE);
CREATE INDEX constructor_country_id_index ON constructor(country_id COLLATE NOCASE);

CREATE TABLE constructor_previous_next_constructor
( constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, previous_next_constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, year_from INTEGER NOT NULL
, year_to INTEGER
, PRIMARY KEY (constructor_id, previous_next_constructor_id, year_from)
);

CREATE INDEX constructor_previous_next_constructor_constructor_id_index ON constructor_previous_next_constructor(constructor_id COLLATE NOCASE);
CREATE INDEX constructor_previous_next_constructor_previous_next_constructor_id_index ON constructor_previous_next_constructor(previous_next_constructor_id COLLATE NOCASE);

CREATE TABLE engine_manufacturer
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
, country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, best_championship_position INTEGER
, best_race_result INTEGER
, best_starting_grid_position INTEGER
, total_championship_wins INTEGER NOT NULL
, total_race_entries INTEGER NOT NULL
, total_race_starts INTEGER NOT NULL
, total_race_wins INTEGER NOT NULL
, total_race_laps INTEGER NOT NULL
, total_podiums INTEGER NOT NULL
, total_podium_races INTEGER NOT NULL
, total_championship_points NUMERIC NOT NULL
, total_pole_positions INTEGER NOT NULL
, total_fastest_laps INTEGER NOT NULL
);

CREATE INDEX engine_manufacturer_name_index ON engine_manufacturer(name COLLATE NOCASE);
CREATE INDEX engine_manufacturer_country_id_index ON engine_manufacturer(country_id COLLATE NOCASE);

CREATE TABLE tyre_manufacturer
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
, country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, best_race_result INTEGER
, best_starting_grid_position INTEGER
, total_race_entries INTEGER NOT NULL
, total_race_starts INTEGER NOT NULL
, total_race_wins INTEGER NOT NULL
, total_race_laps INTEGER NOT NULL
, total_podiums INTEGER NOT NULL
, total_podium_races INTEGER NOT NULL
, total_pole_positions INTEGER NOT NULL
, total_fastest_laps INTEGER NOT NULL
);

CREATE INDEX tyre_manufacturer_name_index ON tyre_manufacturer(name COLLATE NOCASE);
CREATE INDEX tyre_manufacturer_country_id_index ON tyre_manufacturer(country_id COLLATE NOCASE);

CREATE TABLE entrant
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
);

CREATE INDEX entrant_name_index ON entrant(name COLLATE NOCASE);

CREATE TABLE circuit
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
, full_name VARCHAR(255) NOT NULL COLLATE NOCASE
, previous_names VARCHAR(255) COLLATE NOCASE
, type VARCHAR(255) NOT NULL COLLATE NOCASE
, place_name VARCHAR(255) NOT NULL COLLATE NOCASE
, country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, latitude DECIMAL(10,6)
, longitude DECIMAL(10,6)
, total_races_held INTEGER NOT NULL
);

CREATE INDEX circuit_name_index ON circuit(name COLLATE NOCASE);
CREATE INDEX circuit_full_name_index ON circuit(full_name COLLATE NOCASE);
CREATE INDEX circuit_type_index ON circuit(type COLLATE NOCASE);
CREATE INDEX circuit_place_name_index ON circuit(place_name COLLATE NOCASE);
CREATE INDEX circuit_country_id_index ON circuit(country_id COLLATE NOCASE);

CREATE TABLE grand_prix
( id VARCHAR(255) NOT NULL COLLATE NOCASE PRIMARY KEY
, name VARCHAR(255) NOT NULL COLLATE NOCASE
, full_name VARCHAR(255) NOT NULL COLLATE NOCASE
, short_name VARCHAR(255) NOT NULL COLLATE NOCASE
, abbreviation VARCHAR(3) NOT NULL COLLATE NOCASE
, country_id VARCHAR(255) COLLATE NOCASE REFERENCES country(id)
, total_races_held INTEGER NOT NULL
);

CREATE INDEX grand_prix_name_index ON grand_prix(name COLLATE NOCASE);
CREATE INDEX grand_prix_full_name_index ON grand_prix(full_name COLLATE NOCASE);
CREATE INDEX grand_prix_short_name_index ON grand_prix(short_name COLLATE NOCASE);
CREATE INDEX grand_prix_abbreviation_index ON grand_prix(abbreviation COLLATE NOCASE);
CREATE INDEX grand_prix_country_id_index ON grand_prix(country_id COLLATE NOCASE);

CREATE TABLE season
( year INTEGER NOT NULL PRIMARY KEY
);

CREATE TABLE season_entrant
( year INTEGER NOT NULL REFERENCES season(year)
, entrant_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES entrant(id)
, country_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES country(id)
, PRIMARY KEY (year, entrant_id)
);

CREATE INDEX season_entrant_year_index ON season_entrant(year);
CREATE INDEX season_entrant_entrant_id_index ON season_entrant(entrant_id COLLATE NOCASE);
CREATE INDEX season_entrant_country_id_index ON season_entrant(country_id COLLATE NOCASE);

CREATE TABLE season_entrant_constructor
( year INTEGER NOT NULL REFERENCES season(year)
, entrant_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES entrant(id)
, constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, engine_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES engine_manufacturer(id)
, PRIMARY KEY (year, entrant_id, constructor_id, engine_manufacturer_id)
);

CREATE INDEX season_entrant_constructor_year_index ON season_entrant_constructor(year);
CREATE INDEX season_entrant_constructor_entrant_id_index ON season_entrant_constructor(entrant_id COLLATE NOCASE);
CREATE INDEX season_entrant_constructor_constructor_id_index ON season_entrant_constructor(constructor_id COLLATE NOCASE);
CREATE INDEX season_entrant_constructor_engine_manufacturer_id_index ON season_entrant_constructor(engine_manufacturer_id COLLATE NOCASE);

CREATE TABLE season_entrant_tyre_manufacturer
( year INTEGER NOT NULL REFERENCES season(year)
, entrant_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES entrant(id)
, constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, engine_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES engine_manufacturer(id)
, tyre_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES tyre_manufacturer(id)
, PRIMARY KEY (year, entrant_id, constructor_id, engine_manufacturer_id, tyre_manufacturer_id)
);

CREATE INDEX season_entrant_tyre_manufacturer_year_index ON season_entrant_tyre_manufacturer(year);
CREATE INDEX season_entrant_tyre_manufacturer_entrant_id_index ON season_entrant_tyre_manufacturer(entrant_id COLLATE NOCASE);
CREATE INDEX season_entrant_tyre_manufacturer_constructor_id_index ON season_entrant_tyre_manufacturer(constructor_id COLLATE NOCASE);
CREATE INDEX season_entrant_tyre_manufacturer_engine_manufacturer_id_index ON season_entrant_tyre_manufacturer(engine_manufacturer_id COLLATE NOCASE);
CREATE INDEX season_entrant_tyre_manufacturer_tyre_manufacturer_id_index ON season_entrant_tyre_manufacturer(tyre_manufacturer_id COLLATE NOCASE);

CREATE TABLE season_entrant_driver
( year INTEGER NOT NULL REFERENCES season(year)
, entrant_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES entrant(id)
, constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, engine_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES engine_manufacturer(id)
, driver_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES driver(id)
, rounds VARCHAR(255) COLLATE NOCASE
, rounds_text VARCHAR(255) COLLATE NOCASE
, test_driver BOOLEAN NOT NULL
, PRIMARY KEY (year, entrant_id, constructor_id, engine_manufacturer_id, driver_id)
);

CREATE INDEX season_entrant_driver_year_index ON season_entrant_driver(year);
CREATE INDEX season_entrant_driver_entrant_id_index ON season_entrant_driver(entrant_id COLLATE NOCASE);
CREATE INDEX season_entrant_driver_constructor_id_index ON season_entrant_driver(constructor_id COLLATE NOCASE);
CREATE INDEX season_entrant_driver_engine_manufacturer_id_index ON season_entrant_driver(engine_manufacturer_id COLLATE NOCASE);
CREATE INDEX season_entrant_driver_driver_id_index ON season_entrant_driver(driver_id COLLATE NOCASE);

CREATE TABLE season_driver_standing
( year INTEGER NOT NULL REFERENCES season(year)
, position_display_order INTEGER NOT NULL
, position_number INTEGER
, position_text VARCHAR(255) NOT NULL COLLATE NOCASE
, driver_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES driver(id)
, points NUMERIC NOT NULL
, PRIMARY KEY (year, position_display_order)
);

CREATE INDEX season_driver_standing_year_index ON season_driver_standing(year);
CREATE INDEX season_driver_standing_position_display_order_index ON season_driver_standing(position_display_order);
CREATE INDEX season_driver_standing_position_number_index ON season_driver_standing(position_number);
CREATE INDEX season_driver_standing_position_text_index ON season_driver_standing(position_text COLLATE NOCASE);
CREATE INDEX season_driver_standing_driver_id_index ON season_driver_standing(driver_id COLLATE NOCASE);

CREATE TABLE season_constructor_standing
( year INTEGER NOT NULL REFERENCES season(year)
, position_display_order INTEGER NOT NULL
, position_number INTEGER
, position_text VARCHAR(255) NOT NULL COLLATE NOCASE
, constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, engine_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES engine_manufacturer(id)
, points NUMERIC NOT NULL
, PRIMARY KEY (year, position_display_order)
);

CREATE INDEX season_constructor_standing_year_index ON season_constructor_standing(year);
CREATE INDEX season_constructor_standing_position_display_order_index ON season_constructor_standing(position_display_order);
CREATE INDEX season_constructor_standing_position_number_index ON season_constructor_standing(position_number);
CREATE INDEX season_constructor_standing_position_text_index ON season_constructor_standing(position_text COLLATE NOCASE);
CREATE INDEX season_constructor_standing_constructor_id_index ON season_constructor_standing(constructor_id COLLATE NOCASE);
CREATE INDEX season_constructor_standing_engine_manufacturer_id_index ON season_constructor_standing(engine_manufacturer_id COLLATE NOCASE);

CREATE TABLE race
( id INTEGER NOT NULL PRIMARY KEY
, year INTEGER NOT NULL REFERENCES season(year)
, round INTEGER NOT NULL
, date DATE NOT NULL
, grand_prix_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES grand_prix(id)
, official_name VARCHAR(255) NOT NULL COLLATE NOCASE
, qualifying_format VARCHAR(255) NOT NULL COLLATE NOCASE
, sprint_qualifying_format VARCHAR(255) COLLATE NOCASE
, circuit_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES circuit(id)
, circuit_type VARCHAR(255) NOT NULL COLLATE NOCASE
, course_length DECIMAL(6,3) NOT NULL
, laps INTEGER NOT NULL
, distance DECIMAL(6,3) NOT NULL
, scheduled_laps INTEGER
, scheduled_distance DECIMAL(6,3)
, UNIQUE (year, round)
);

CREATE INDEX race_year_index ON race(year);
CREATE INDEX race_round_index ON race(round);
CREATE INDEX race_date_index ON race (date);
CREATE INDEX race_grand_prix_id_index ON race(grand_prix_id COLLATE NOCASE);
CREATE INDEX race_official_name_index ON race(official_name COLLATE NOCASE);
CREATE INDEX race_circuit_id_index ON race(circuit_id COLLATE NOCASE);

CREATE TABLE race_data
( race_id INTEGER NOT NULL REFERENCES race(id)
, type VARCHAR(255) NOT NULL COLLATE NOCASE
, position_display_order INTEGER NOT NULL
, position_number INTEGER
, position_text VARCHAR(255) COLLATE NOCASE
, driver_number VARCHAR(255) NOT NULL COLLATE NOCASE
, driver_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES driver(id)
, constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, engine_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES engine_manufacturer(id)
, tyre_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES tyre_manufacturer(id)
, practice_time VARCHAR(255) COLLATE NOCASE
, practice_time_millis INTEGER
, practice_gap VARCHAR(255) COLLATE NOCASE
, practice_gap_millis INTEGER
, practice_interval VARCHAR(255) COLLATE NOCASE
, practice_interval_millis INTEGER
, practice_laps INTEGER
, qualifying_time VARCHAR(255) COLLATE NOCASE
, qualifying_time_millis INTEGER
, qualifying_q1 VARCHAR(255) COLLATE NOCASE
, qualifying_q1_millis INTEGER
, qualifying_q2 VARCHAR(255) COLLATE NOCASE
, qualifying_q2_millis INTEGER
, qualifying_q3 VARCHAR(255) COLLATE NOCASE
, qualifying_q3_millis INTEGER
, qualifying_gap VARCHAR(255) COLLATE NOCASE
, qualifying_gap_millis INTEGER
, qualifying_interval VARCHAR(255) COLLATE NOCASE
, qualifying_interval_millis INTEGER
, qualifying_laps INTEGER
, starting_grid_position_grid_penalty VARCHAR(255) COLLATE NOCASE
, starting_grid_position_grid_penalty_positions INTEGER
, starting_grid_position_time VARCHAR(255) COLLATE NOCASE
, starting_grid_position_time_millis INTEGER
, race_shared_car BOOLEAN
, race_laps INTEGER
, race_time VARCHAR(255) COLLATE NOCASE
, race_time_millis INTEGER
, race_time_penalty VARCHAR(255) COLLATE NOCASE
, race_time_penalty_millis INTEGER
, race_gap VARCHAR(255) COLLATE NOCASE
, race_gap_millis INTEGER
, race_gap_laps INTEGER
, race_interval VARCHAR(255) COLLATE NOCASE
, race_interval_millis INTEGER
, race_reason_retired VARCHAR(255) COLLATE NOCASE
, race_points NUMERIC
, race_grid_position_number INTEGER
, race_grid_position_text VARCHAR(255) COLLATE NOCASE
, race_positions_gained INTEGER
, race_pit_stops INTEGER
, race_fastest_lap BOOLEAN
, race_driver_of_the_day BOOLEAN
, race_grand_slam BOOLEAN
, fastest_lap_lap INTEGER
, fastest_lap_time VARCHAR(255) COLLATE NOCASE
, fastest_lap_time_millis INTEGER
, fastest_lap_gap VARCHAR(255) COLLATE NOCASE
, fastest_lap_gap_millis INTEGER
, fastest_lap_interval VARCHAR(255) COLLATE NOCASE
, fastest_lap_interval_millis INTEGER
, pit_stop_stop INTEGER
, pit_stop_lap INTEGER
, pit_stop_time VARCHAR(255) COLLATE NOCASE
, pit_stop_time_millis INTEGER
, driver_of_the_day_percentage NUMERIC
, PRIMARY KEY (race_id, type, position_display_order)
);

CREATE INDEX race_data_race_id_index ON race_data(race_id);
CREATE INDEX race_data_type_index ON race_data(type COLLATE NOCASE);
CREATE INDEX race_data_position_number_index ON race_data(position_number);
CREATE INDEX race_data_position_text_index ON race_data(position_text COLLATE NOCASE);
CREATE INDEX race_data_driver_number_index ON race_data(driver_number COLLATE NOCASE);
CREATE INDEX race_data_driver_id_index ON race_data(driver_id COLLATE NOCASE);
CREATE INDEX race_data_constructor_id_index ON race_data(constructor_id COLLATE NOCASE);
CREATE INDEX race_data_engine_manufacturer_id_index ON race_data(engine_manufacturer_id COLLATE NOCASE);
CREATE INDEX race_data_tyre_manufacturer_id_index ON race_data(tyre_manufacturer_id COLLATE NOCASE);

CREATE TABLE race_driver_standing
( race_id INTEGER NOT NULL REFERENCES race(id)
, position_display_order INTEGER NOT NULL
, position_number INTEGER
, position_text VARCHAR(255) NOT NULL COLLATE NOCASE
, driver_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES driver(id)
, points NUMERIC NOT NULL
, positions_gained INTEGER
, PRIMARY KEY (race_id, position_display_order)
);

CREATE INDEX race_driver_standing_race_id_index ON race_driver_standing(race_id);
CREATE INDEX race_driver_standing_position_display_order_index ON race_driver_standing(position_display_order);
CREATE INDEX race_driver_standing_position_number_index ON race_driver_standing(position_number);
CREATE INDEX race_driver_standing_position_text_index ON race_driver_standing(position_text COLLATE NOCASE);
CREATE INDEX race_driver_standing_driver_id_index ON race_driver_standing(driver_id COLLATE NOCASE);

CREATE TABLE race_constructor_standing
( race_id INTEGER NOT NULL REFERENCES race(id)
, position_display_order INTEGER NOT NULL
, position_number INTEGER
, position_text VARCHAR(255) NOT NULL COLLATE NOCASE
, constructor_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES constructor(id)
, engine_manufacturer_id VARCHAR(255) NOT NULL COLLATE NOCASE REFERENCES engine_manufacturer(id)
, points NUMERIC NOT NULL
, positions_gained INTEGER
, PRIMARY KEY (race_id, position_display_order)
);

CREATE INDEX race_constructor_standing_race_id_index ON race_constructor_standing(race_id);
CREATE INDEX race_constructor_standing_position_display_order_index ON race_constructor_standing(position_display_order);
CREATE INDEX race_constructor_standing_position_number_index ON race_constructor_standing(position_number);
CREATE INDEX race_constructor_standing_position_text_index ON race_constructor_standing(position_text COLLATE NOCASE);
CREATE INDEX race_constructor_standing_constructor_id_index ON race_constructor_standing(constructor_id COLLATE NOCASE);
CREATE INDEX race_constructor_standing_engine_manufacturer_id_index ON race_constructor_standing(engine_manufacturer_id COLLATE NOCASE);

CREATE VIEW pre_qualifying_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.qualifying_time AS time
,        race_data.qualifying_time_millis AS time_millis
,        race_data.qualifying_gap AS gap
,        race_data.qualifying_gap_millis AS gap_millis
,        race_data.qualifying_interval AS interval
,        race_data.qualifying_interval_millis AS interval_millis
,        race_data.qualifying_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'PRE_QUALIFYING_RESULT'
;

CREATE VIEW free_practice_1_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.practice_time AS time
,        race_data.practice_time_millis AS time_millis
,        race_data.practice_gap AS gap
,        race_data.practice_gap_millis AS gap_millis
,        race_data.practice_interval AS interval
,        race_data.practice_interval_millis AS interval_millis
,        race_data.practice_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'FREE_PRACTICE_1_RESULT'
;

CREATE VIEW free_practice_2_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.practice_time AS time
,        race_data.practice_time_millis AS time_millis
,        race_data.practice_gap AS gap
,        race_data.practice_gap_millis AS gap_millis
,        race_data.practice_interval AS interval
,        race_data.practice_interval_millis AS interval_millis
,        race_data.practice_laps AS laps
FROM     race_data
JOIN     race ON race_data.race_id = race.id
WHERE    race_data.type = 'FREE_PRACTICE_2_RESULT'
;

CREATE VIEW free_practice_3_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.practice_time AS time
,        race_data.practice_time_millis AS time_millis
,        race_data.practice_gap AS gap
,        race_data.practice_gap_millis AS gap_millis
,        race_data.practice_interval AS interval
,        race_data.practice_interval_millis AS interval_millis
,        race_data.practice_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'FREE_PRACTICE_3_RESULT'
;

CREATE VIEW free_practice_4_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.practice_time AS time
,        race_data.practice_time_millis AS time_millis
,        race_data.practice_gap AS gap
,        race_data.practice_gap_millis AS gap_millis
,        race_data.practice_interval AS interval
,        race_data.practice_interval_millis AS interval_millis
,        race_data.practice_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'FREE_PRACTICE_4_RESULT'
;

CREATE VIEW qualifying_1_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.qualifying_time AS time
,        race_data.qualifying_time_millis AS time_millis
,        race_data.qualifying_gap AS gap
,        race_data.qualifying_gap_millis AS gap_millis
,        race_data.qualifying_interval AS interval
,        race_data.qualifying_interval_millis AS interval_millis
,        race_data.qualifying_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'QUALIFYING_1_RESULT'
;

CREATE VIEW qualifying_2_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.qualifying_time AS time
,        race_data.qualifying_time_millis AS time_millis
,        race_data.qualifying_gap AS gap
,        race_data.qualifying_gap_millis AS gap_millis
,        race_data.qualifying_interval AS interval
,        race_data.qualifying_interval_millis AS interval_millis
,        race_data.qualifying_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'QUALIFYING_2_RESULT'
;

CREATE VIEW qualifying_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.qualifying_time AS time
,        race_data.qualifying_time_millis AS time_millis
,        race_data.qualifying_q1 AS q1
,        race_data.qualifying_q1_millis AS q1_millis
,        race_data.qualifying_q2 AS q2
,        race_data.qualifying_q2_millis AS q2_millis
,        race_data.qualifying_q3 AS q3
,        race_data.qualifying_q3_millis AS q3_millis
,        race_data.qualifying_gap AS gap
,        race_data.qualifying_gap_millis AS gap_millis
,        race_data.qualifying_interval AS interval
,        race_data.qualifying_interval_millis AS interval_millis
,        race_data.qualifying_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'QUALIFYING_RESULT'
;

CREATE VIEW sprint_qualifying_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.qualifying_time AS time
,        race_data.qualifying_time_millis AS time_millis
,        race_data.qualifying_q1 AS q1
,        race_data.qualifying_q1_millis AS q1_millis
,        race_data.qualifying_q2 AS q2
,        race_data.qualifying_q2_millis AS q2_millis
,        race_data.qualifying_q3 AS q3
,        race_data.qualifying_q3_millis AS q3_millis
,        race_data.qualifying_gap AS gap
,        race_data.qualifying_gap_millis AS gap_millis
,        race_data.qualifying_interval AS interval
,        race_data.qualifying_interval_millis AS interval_millis
,        race_data.qualifying_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'SPRINT_QUALIFYING_RESULT'
;

CREATE VIEW sprint_starting_grid_position AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.starting_grid_position_grid_penalty AS grid_penalty
,        race_data.starting_grid_position_grid_penalty_positions AS grid_penalty_positions
,        race_data.starting_grid_position_time AS time
,        race_data.starting_grid_position_time_millis AS time_millis
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'SPRINT_STARTING_GRID_POSITION'
;

CREATE VIEW sprint_race_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.race_laps AS laps
,        race_data.race_time AS time
,        race_data.race_time_millis AS time_millis
,        race_data.race_time_penalty AS time_penalty
,        race_data.race_time_penalty_millis AS time_penalty_millis
,        race_data.race_gap AS gap
,        race_data.race_gap_millis AS gap_millis
,        race_data.race_gap_laps AS gap_laps
,        race_data.race_interval AS interval
,        race_data.race_interval_millis AS interval_millis
,        race_data.race_reason_retired AS reason_retired
,        race_data.race_points AS points
,        race_data.race_grid_position_number AS grid_position_number
,        race_data.race_grid_position_text AS grid_position_text
,        race_data.race_positions_gained AS positions_gained
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'SPRINT_RACE_RESULT'
;

CREATE VIEW warming_up_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.practice_time AS time
,        race_data.practice_time_millis AS time_millis
,        race_data.practice_gap AS gap
,        race_data.practice_gap_millis AS gap_millis
,        race_data.practice_interval AS interval
,        race_data.practice_interval_millis AS interval_millis
,        race_data.practice_laps AS laps
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'WARMING_UP_RESULT'
;

CREATE VIEW starting_grid_position AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.starting_grid_position_grid_penalty AS grid_penalty
,        race_data.starting_grid_position_grid_penalty_positions AS grid_penalty_positions
,        race_data.starting_grid_position_time AS time
,        race_data.starting_grid_position_time_millis AS time_millis
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'STARTING_GRID_POSITION'
;

CREATE VIEW race_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.race_shared_car AS shared_car
,        race_data.race_laps AS laps
,        race_data.race_time AS time
,        race_data.race_time_millis AS time_millis
,        race_data.race_time_penalty AS time_penalty
,        race_data.race_time_penalty_millis AS time_penalty_millis
,        race_data.race_gap AS gap
,        race_data.race_gap_millis AS gap_millis
,        race_data.race_gap_laps AS gap_laps
,        race_data.race_interval AS interval
,        race_data.race_interval_millis AS interval_millis
,        race_data.race_reason_retired AS reason_retired
,        race_data.race_points AS points
,        race_data.race_grid_position_number AS grid_position_number
,        race_data.race_grid_position_text AS grid_position_text
,        race_data.race_positions_gained AS positions_gained
,        race_data.race_pit_stops AS pit_stops
,        race_data.race_fastest_lap AS fastest_lap
,        race_data.race_driver_of_the_day AS driver_of_the_day
,        race_data.race_grand_slam AS grand_slam
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'RACE_RESULT'
;

CREATE VIEW fastest_lap AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.fastest_lap_lap AS lap
,        race_data.fastest_lap_time AS time
,        race_data.fastest_lap_time_millis AS time_millis
,        race_data.fastest_lap_gap AS gap
,        race_data.fastest_lap_gap_millis AS gap_millis
,        race_data.fastest_lap_interval AS interval
,        race_data.fastest_lap_interval_millis AS interval_millis
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'FASTEST_LAP'
;

CREATE VIEW pit_stop AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.pit_stop_stop AS stop
,        race_data.pit_stop_lap AS lap
,        race_data.pit_stop_time AS time
,        race_data.pit_stop_time_millis AS time_millis
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'PIT_STOP'
;

CREATE VIEW driver_of_the_day_result AS
SELECT   race.id AS race_id
,        race.year
,        race.round
,        race_data.position_display_order
,        race_data.position_number
,        race_data.position_text
,        race_data.driver_number
,        race_data.driver_id
,        race_data.constructor_id
,        race_data.engine_manufacturer_id
,        race_data.tyre_manufacturer_id
,        race_data.driver_of_the_day_percentage AS percentage
FROM     race_data
JOIN     race
ON       race_data.race_id = race.id
WHERE    race_data.type    = 'DRIVER_OF_THE_DAY_RESULT'
;
