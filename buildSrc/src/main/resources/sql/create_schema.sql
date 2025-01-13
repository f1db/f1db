CREATE TABLE "continent"
( "id" VARCHAR(100) NOT NULL
, "code" VARCHAR(2) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "demonym" VARCHAR(100) NOT NULL
, CONSTRAINT "cntn_pk" PRIMARY KEY ("id")
, CONSTRAINT "cntn_code_uk" UNIQUE ("code")
, CONSTRAINT "cntn_name_uk" UNIQUE ("name")
);

CREATE TABLE "country"
( "id" VARCHAR(100) NOT NULL
, "alpha2_code" VARCHAR(2) NOT NULL
, "alpha3_code" VARCHAR(3) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "demonym" VARCHAR(100)
, "continent_id" VARCHAR(100) NOT NULL
, CONSTRAINT "cntr_pk" PRIMARY KEY ("id")
, CONSTRAINT "cntr_alpha2_code_uk" UNIQUE ("alpha2_code")
, CONSTRAINT "cntr_alpha3_code_uk" UNIQUE ("alpha3_code")
, CONSTRAINT "cntr_name_uk" UNIQUE ("name")
, CONSTRAINT "cntr_continent_id_fk" FOREIGN KEY ("continent_id") REFERENCES "continent" ("id")
);

CREATE INDEX "cntr_continent_id_idx" ON "country" ("continent_id");

CREATE TABLE "driver"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "first_name" VARCHAR(100) NOT NULL
, "last_name" VARCHAR(100) NOT NULL
, "full_name" VARCHAR(100) NOT NULL
, "abbreviation" VARCHAR(3) NOT NULL
, "permanent_number" VARCHAR(2)
, "gender" VARCHAR(6) NOT NULL
, "date_of_birth" DATE NOT NULL
, "date_of_death" DATE
, "place_of_birth" VARCHAR(100) NOT NULL
, "country_of_birth_country_id" VARCHAR(100) NOT NULL
, "nationality_country_id" VARCHAR(100) NOT NULL
, "second_nationality_country_id" VARCHAR(100)
, "best_championship_position" INTEGER
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_championship_wins" INTEGER NOT NULL
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_points" DECIMAL(8,2) NOT NULL
, "total_championship_points" DECIMAL(8,2) NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, "total_driver_of_the_day" INTEGER NOT NULL
, "total_grand_slams" INTEGER NOT NULL
, CONSTRAINT "drvr_pk" PRIMARY KEY ("id")
, CONSTRAINT "drvr_country_of_birth_country_id_fk" FOREIGN KEY ("country_of_birth_country_id") REFERENCES "country" ("id")
, CONSTRAINT "drvr_nationality_country_id_fk" FOREIGN KEY ("nationality_country_id") REFERENCES "country" ("id")
, CONSTRAINT "drvr_second_nationality_country_id_fk" FOREIGN KEY ("second_nationality_country_id") REFERENCES "country" ("id")
);

CREATE INDEX "drvr_name_idx" ON "driver" ("name");
CREATE INDEX "drvr_first_name_idx" ON "driver" ("first_name");
CREATE INDEX "drvr_last_name_idx" ON "driver" ("last_name");
CREATE INDEX "drvr_full_name_idx" ON "driver" ("full_name");
CREATE INDEX "drvr_abbreviation_idx" ON "driver" ("abbreviation");
CREATE INDEX "drvr_permanent_number_idx" ON "driver" ("permanent_number");
CREATE INDEX "drvr_gender_idx" ON "driver" ("gender");
CREATE INDEX "drvr_date_of_birth_idx" ON "driver" ("date_of_birth");
CREATE INDEX "drvr_date_of_death_idx" ON "driver" ("date_of_death");
CREATE INDEX "drvr_place_of_birth_idx" ON "driver" ("place_of_birth");
CREATE INDEX "drvr_country_of_birth_country_id_idx" ON "driver" ("country_of_birth_country_id");
CREATE INDEX "drvr_nationality_country_id_idx" ON "driver" ("nationality_country_id");
CREATE INDEX "drvr_second_nationality_country_id_idx" ON "driver" ("second_nationality_country_id");

CREATE TABLE "driver_family_relationship"
( "driver_id" VARCHAR(100) NOT NULL
, "position_display_order" INTEGER NOT NULL
, "other_driver_id" VARCHAR(100) NOT NULL
, "type" VARCHAR(50) NOT NULL
, CONSTRAINT "dfrl_pk" PRIMARY KEY ("driver_id", "position_display_order")
, CONSTRAINT "dfrl_driver_id_other_driver_id_type_uk" UNIQUE ("driver_id", "other_driver_id", "type")
, CONSTRAINT "dfrl_driver_id_fk" FOREIGN KEY ("driver_id") REFERENCES "driver" ("id")
, CONSTRAINT "dfrl_other_driver_id_fk" FOREIGN KEY ("other_driver_id") REFERENCES "driver" ("id")
);

CREATE INDEX "dfrl_driver_id_idx" ON "driver_family_relationship" ("driver_id");
CREATE INDEX "dfrl_position_display_order_idx" ON "driver_family_relationship" ("position_display_order");
CREATE INDEX "dfrl_other_driver_id_idx" ON "driver_family_relationship" ("other_driver_id");

CREATE TABLE "constructor"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "full_name" VARCHAR(100) NOT NULL
, "country_id" VARCHAR(100) NOT NULL
, "best_championship_position" INTEGER
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_championship_wins" INTEGER NOT NULL
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_1_and_2_finishes" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_podium_races" INTEGER NOT NULL
, "total_points" DECIMAL(8,2) NOT NULL
, "total_championship_points" DECIMAL(8,2) NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, CONSTRAINT "cnst_pk" PRIMARY KEY ("id")
, CONSTRAINT "cnst_country_id_fk" FOREIGN KEY ("country_id") REFERENCES "country" ("id")
);

CREATE INDEX "cnst_name_idx" ON "constructor" ("name");
CREATE INDEX "cnst_full_name_idx" ON "constructor" ("full_name");
CREATE INDEX "cnst_country_id_idx" ON "constructor" ("country_id");

CREATE TABLE "constructor_chronology"
( "constructor_id" VARCHAR(100) NOT NULL
, "position_display_order" INTEGER NOT NULL
, "other_constructor_id" VARCHAR(100) NOT NULL
, "year_from" INTEGER NOT NULL
, "year_to" INTEGER
, CONSTRAINT "cnch_pk" PRIMARY KEY ("constructor_id", "position_display_order")
, CONSTRAINT "cnch_constructor_id_other_constructor_id_year_from_year_to_uk" UNIQUE ("constructor_id", "other_constructor_id", "year_from", "year_to")
, CONSTRAINT "cnch_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "cnch_other_constructor_id_fk" FOREIGN KEY ("other_constructor_id") REFERENCES "constructor" ("id")
);

CREATE INDEX "cnch_constructor_id_idx" ON "constructor_chronology" ("constructor_id");
CREATE INDEX "cnch_position_display_order_idx" ON "constructor_chronology" ("position_display_order");
CREATE INDEX "cnch_other_constructor_id_idx" ON "constructor_chronology" ("other_constructor_id");

CREATE TABLE "chassis"
( "id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "full_name" VARCHAR(100) NOT NULL
, CONSTRAINT "chss_pk" PRIMARY KEY ("id")
, CONSTRAINT "chss_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
);

CREATE INDEX "chss_constructor_id_idx" ON "chassis" ("constructor_id");
CREATE INDEX "chss_name_idx" ON "chassis" ("name");
CREATE INDEX "chss_full_name_idx" ON "chassis" ("full_name");

CREATE TABLE "engine_manufacturer"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "country_id" VARCHAR(100) NOT NULL
, "best_championship_position" INTEGER
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_championship_wins" INTEGER NOT NULL
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_podium_races" INTEGER NOT NULL
, "total_points" DECIMAL(8,2) NOT NULL
, "total_championship_points" DECIMAL(8,2) NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, CONSTRAINT "enmf_pk" PRIMARY KEY ("id")
, CONSTRAINT "enmf_country_id_fk" FOREIGN KEY ("country_id") REFERENCES "country" ("id")
);

CREATE INDEX "enmf_name_idx" ON "engine_manufacturer" ("name");
CREATE INDEX "enmf_country_id_idx" ON "engine_manufacturer" ("country_id");

CREATE TABLE "engine"
( "id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "full_name" VARCHAR(100) NOT NULL
, "capacity" DECIMAL(2,1)
, "configuration" VARCHAR(100)
, "aspiration" VARCHAR(100)
, CONSTRAINT "engn_pk" PRIMARY KEY ("id")
, CONSTRAINT "engn_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
);

CREATE INDEX "engn_engine_manufacturer_id_idx" ON "engine" ("engine_manufacturer_id");
CREATE INDEX "engn_name_idx" ON "engine" ("name");
CREATE INDEX "engn_full_name_idx" ON "engine" ("full_name");
CREATE INDEX "engn_capacity_idx" ON "engine" ("capacity");
CREATE INDEX "engn_configuration_idx" ON "engine" ("configuration");
CREATE INDEX "engn_aspiration_idx" ON "engine" ("aspiration");

CREATE TABLE "tyre_manufacturer"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "country_id" VARCHAR(100) NOT NULL
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_podium_races" INTEGER NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, CONSTRAINT "tymf_pk" PRIMARY KEY ("id")
, CONSTRAINT "tymf_country_id_fk" FOREIGN KEY ("country_id") REFERENCES "country" ("id")
);

CREATE INDEX "tymf_name_idx" ON "tyre_manufacturer" ("name");
CREATE INDEX "tymf_country_id_idx" ON "tyre_manufacturer" ("country_id");

CREATE TABLE "entrant"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, CONSTRAINT "entr_pk" PRIMARY KEY ("id")
);

CREATE INDEX "entr_name_idx" ON "entrant" ("name");

CREATE TABLE "circuit"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "full_name" VARCHAR(100) NOT NULL
, "previous_names" VARCHAR(255)
, "type" VARCHAR(6) NOT NULL
, "direction" VARCHAR(14) NOT NULL
, "place_name" VARCHAR(100) NOT NULL
, "country_id" VARCHAR(100) NOT NULL
, "latitude" DECIMAL(10,6) NOT NULL
, "longitude" DECIMAL(10,6) NOT NULL
, "length" DECIMAL(6,3) NOT NULL
, "turns" INTEGER NOT NULL
, "total_races_held" INTEGER NOT NULL
, CONSTRAINT "crct_pk" PRIMARY KEY ("id")
, CONSTRAINT "crct_country_id_fk" FOREIGN KEY ("country_id") REFERENCES "country" ("id")
);

CREATE INDEX "crct_name_idx" ON "circuit" ("name");
CREATE INDEX "crct_full_name_idx" ON "circuit" ("full_name");
CREATE INDEX "crct_type_idx" ON "circuit" ("type");
CREATE INDEX "crct_direction_idx" ON "circuit" ("direction");
CREATE INDEX "crct_place_name_idx" ON "circuit" ("place_name");
CREATE INDEX "crct_country_id_idx" ON "circuit" ("country_id");

CREATE TABLE "grand_prix"
( "id" VARCHAR(100) NOT NULL
, "name" VARCHAR(100) NOT NULL
, "full_name" VARCHAR(100) NOT NULL
, "short_name" VARCHAR(100) NOT NULL
, "abbreviation" VARCHAR(3) NOT NULL
, "country_id" VARCHAR(100)
, "total_races_held" INTEGER NOT NULL
, CONSTRAINT "grpx_pk" PRIMARY KEY ("id")
, CONSTRAINT "grpx_country_id_fk" FOREIGN KEY ("country_id") REFERENCES "country" ("id")
);

CREATE INDEX "grpx_name_idx" ON "grand_prix" ("name");
CREATE INDEX "grpx_full_name_idx" ON "grand_prix" ("full_name");
CREATE INDEX "grpx_short_name_idx" ON "grand_prix" ("short_name");
CREATE INDEX "grpx_abbreviation_idx" ON "grand_prix" ("abbreviation");
CREATE INDEX "grpx_country_id_idx" ON "grand_prix" ("country_id");

CREATE TABLE "season"
( "year" INTEGER NOT NULL
, CONSTRAINT "sson_pk" PRIMARY KEY ("year")
);

CREATE TABLE "season_entrant"
( "year" INTEGER NOT NULL
, "entrant_id" VARCHAR(100) NOT NULL
, "country_id" VARCHAR(100) NOT NULL
, CONSTRAINT "sent_pk" PRIMARY KEY ("year", "entrant_id")
, CONSTRAINT "sent_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "sent_entrant_id_fk" FOREIGN KEY ("entrant_id") REFERENCES "entrant" ("id")
, CONSTRAINT "sent_country_id_fk" FOREIGN KEY ("country_id") REFERENCES "country" ("id")
);

CREATE INDEX "sent_year_idx" ON "season_entrant" ("year");
CREATE INDEX "sent_entrant_id_idx" ON "season_entrant" ("entrant_id");
CREATE INDEX "sent_country_id_idx" ON "season_entrant" ("country_id");

CREATE TABLE "season_entrant_constructor"
( "year" INTEGER NOT NULL
, "entrant_id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, CONSTRAINT "secn_pk" PRIMARY KEY ("year", "entrant_id", "constructor_id", "engine_manufacturer_id")
, CONSTRAINT "secn_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "secn_entrant_id_fk" FOREIGN KEY ("entrant_id") REFERENCES "entrant" ("id")
, CONSTRAINT "secn_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "secn_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
);

CREATE INDEX "secn_year_idx" ON "season_entrant_constructor" ("year");
CREATE INDEX "secn_entrant_id_idx" ON "season_entrant_constructor" ("entrant_id");
CREATE INDEX "secn_constructor_id_idx" ON "season_entrant_constructor" ("constructor_id");
CREATE INDEX "secn_engine_manufacturer_id_idx" ON "season_entrant_constructor" ("engine_manufacturer_id");

CREATE TABLE "season_entrant_chassis"
( "year" INTEGER NOT NULL
, "entrant_id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "chassis_id" VARCHAR(100) NOT NULL
, CONSTRAINT "sech_pk" PRIMARY KEY ("year", "entrant_id", "constructor_id", "engine_manufacturer_id", "chassis_id")
, CONSTRAINT "sech_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "sech_entrant_id_fk" FOREIGN KEY ("entrant_id") REFERENCES "entrant" ("id")
, CONSTRAINT "sech_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "sech_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
, CONSTRAINT "sech_chassis_id_fk" FOREIGN KEY ("chassis_id") REFERENCES "chassis" ("id")
);

CREATE INDEX "sech_year_idx" ON "season_entrant_chassis" ("year");
CREATE INDEX "sech_entrant_id_idx" ON "season_entrant_chassis" ("entrant_id");
CREATE INDEX "sech_constructor_id_idx" ON "season_entrant_chassis" ("constructor_id");
CREATE INDEX "sech_engine_manufacturer_id_idx" ON "season_entrant_chassis" ("engine_manufacturer_id");
CREATE INDEX "sech_chassis_id_idx" ON "season_entrant_chassis" ("chassis_id");

CREATE TABLE "season_entrant_engine"
( "year" INTEGER NOT NULL
, "entrant_id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "engine_id" VARCHAR(100) NOT NULL
, CONSTRAINT "seen_pk" PRIMARY KEY ("year", "entrant_id", "constructor_id", "engine_manufacturer_id", "engine_id")
, CONSTRAINT "seen_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "seen_entrant_id_fk" FOREIGN KEY ("entrant_id") REFERENCES "entrant" ("id")
, CONSTRAINT "seen_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "seen_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
, CONSTRAINT "seen_engine_id_fk" FOREIGN KEY ("engine_id") REFERENCES "engine" ("id")
);

CREATE INDEX "seen_year_idx" ON "season_entrant_engine" ("year");
CREATE INDEX "seen_entrant_id_idx" ON "season_entrant_engine" ("entrant_id");
CREATE INDEX "seen_constructor_id_idx" ON "season_entrant_engine" ("constructor_id");
CREATE INDEX "seen_engine_manufacturer_id_idx" ON "season_entrant_engine" ("engine_manufacturer_id");
CREATE INDEX "seen_engine_id_idx" ON "season_entrant_engine" ("engine_id");

CREATE TABLE "season_entrant_tyre_manufacturer"
( "year" INTEGER NOT NULL
, "entrant_id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "tyre_manufacturer_id" VARCHAR(100) NOT NULL
, CONSTRAINT "setm_pk" PRIMARY KEY ("year", "entrant_id", "constructor_id", "engine_manufacturer_id", "tyre_manufacturer_id")
, CONSTRAINT "setm_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "setm_entrant_id_fk" FOREIGN KEY ("entrant_id") REFERENCES "entrant" ("id")
, CONSTRAINT "setm_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "setm_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
, CONSTRAINT "setm_tyre_manufacturer_id_fk" FOREIGN KEY ("tyre_manufacturer_id") REFERENCES "tyre_manufacturer" ("id")
);

CREATE INDEX "setm_year_idx" ON "season_entrant_tyre_manufacturer" ("year");
CREATE INDEX "setm_entrant_id_idx" ON "season_entrant_tyre_manufacturer" ("entrant_id");
CREATE INDEX "setm_constructor_id_idx" ON "season_entrant_tyre_manufacturer" ("constructor_id");
CREATE INDEX "setm_engine_manufacturer_id_idx" ON "season_entrant_tyre_manufacturer" ("engine_manufacturer_id");
CREATE INDEX "setm_tyre_manufacturer_id_idx" ON "season_entrant_tyre_manufacturer" ("tyre_manufacturer_id");

CREATE TABLE "season_entrant_driver"
( "year" INTEGER NOT NULL
, "entrant_id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "driver_id" VARCHAR(100) NOT NULL
, "rounds" VARCHAR(100)
, "rounds_text" VARCHAR(100)
, "test_driver" BOOLEAN NOT NULL
, CONSTRAINT "sedr_pk" PRIMARY KEY ("year", "entrant_id", "constructor_id", "engine_manufacturer_id", "driver_id")
, CONSTRAINT "sedr_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "sedr_entrant_id_fk" FOREIGN KEY ("entrant_id") REFERENCES "entrant" ("id")
, CONSTRAINT "sedr_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "sedr_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
, CONSTRAINT "sedr_driver_id_fk" FOREIGN KEY ("driver_id") REFERENCES "driver" ("id")
);

CREATE INDEX "sedr_year_idx" ON "season_entrant_driver" ("year");
CREATE INDEX "sedr_entrant_id_idx" ON "season_entrant_driver" ("entrant_id");
CREATE INDEX "sedr_constructor_id_idx" ON "season_entrant_driver" ("constructor_id");
CREATE INDEX "sedr_engine_manufacturer_id_idx" ON "season_entrant_driver" ("engine_manufacturer_id");
CREATE INDEX "sedr_driver_id_idx" ON "season_entrant_driver" ("driver_id");

CREATE TABLE "season_constructor"
( "year" INTEGER NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4)
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_1_and_2_finishes" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_podium_races" INTEGER NOT NULL
, "total_points" DECIMAL(8,2) NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, CONSTRAINT "sscn_pk" PRIMARY KEY ("year", "constructor_id")
, CONSTRAINT "sscn_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "sscn_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
);

CREATE INDEX "sscn_year_idx" ON "season_constructor" ("year");
CREATE INDEX "sscn_constructor_id_idx" ON "season_constructor" ("constructor_id");

CREATE TABLE "season_engine_manufacturer"
( "year" INTEGER NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4)
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_podium_races" INTEGER NOT NULL
, "total_points" DECIMAL(8,2) NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, CONSTRAINT "ssem_pk" PRIMARY KEY ("year", "engine_manufacturer_id")
, CONSTRAINT "ssem_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "ssem_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
);

CREATE INDEX "ssem_year_idx" ON "season_engine_manufacturer" ("year");
CREATE INDEX "ssem_engine_manufacturer_id_idx" ON "season_engine_manufacturer" ("engine_manufacturer_id");

CREATE TABLE "season_tyre_manufacturer"
( "year" INTEGER NOT NULL
, "tyre_manufacturer_id" VARCHAR(100) NOT NULL
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_podium_races" INTEGER NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, CONSTRAINT "sstm_pk" PRIMARY KEY ("year", "tyre_manufacturer_id")
, CONSTRAINT "sstm_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "sstm_tyre_manufacturer_id_fk" FOREIGN KEY ("tyre_manufacturer_id") REFERENCES "tyre_manufacturer" ("id")
);

CREATE INDEX "sstm_year_idx" ON "season_tyre_manufacturer" ("year");
CREATE INDEX "sstm_tyre_manufacturer_id_idx" ON "season_tyre_manufacturer" ("tyre_manufacturer_id");

CREATE TABLE "season_driver"
( "year" INTEGER NOT NULL
, "driver_id" VARCHAR(100) NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4)
, "best_starting_grid_position" INTEGER
, "best_race_result" INTEGER
, "total_race_entries" INTEGER NOT NULL
, "total_race_starts" INTEGER NOT NULL
, "total_race_wins" INTEGER NOT NULL
, "total_race_laps" INTEGER NOT NULL
, "total_podiums" INTEGER NOT NULL
, "total_points" DECIMAL(8,2) NOT NULL
, "total_pole_positions" INTEGER NOT NULL
, "total_fastest_laps" INTEGER NOT NULL
, "total_driver_of_the_day" INTEGER NOT NULL
, "total_grand_slams" INTEGER NOT NULL
, CONSTRAINT "ssdr_pk" PRIMARY KEY ("year", "driver_id")
, CONSTRAINT "ssdr_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "ssdr_driver_id_fk" FOREIGN KEY ("driver_id") REFERENCES "driver" ("id")
);

CREATE INDEX "ssdr_year_idx" ON "season_driver" ("year");
CREATE INDEX "ssdr_driver_id_idx" ON "season_driver" ("driver_id");

CREATE TABLE "season_driver_standing"
( "year" INTEGER NOT NULL
, "position_display_order" INTEGER NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4) NOT NULL
, "driver_id" VARCHAR(100) NOT NULL
, "points" DECIMAL(8,2) NOT NULL
, CONSTRAINT "ssds_pk" PRIMARY KEY ("year", "position_display_order")
, CONSTRAINT "ssds_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "ssds_driver_id_fk" FOREIGN KEY ("driver_id") REFERENCES "driver" ("id")
);

CREATE INDEX "ssds_year_idx" ON "season_driver_standing" ("year");
CREATE INDEX "ssds_position_display_order_idx" ON "season_driver_standing" ("position_display_order");
CREATE INDEX "ssds_position_number_idx" ON "season_driver_standing" ("position_number");
CREATE INDEX "ssds_position_text_idx" ON "season_driver_standing" ("position_text");
CREATE INDEX "ssds_driver_id_idx" ON "season_driver_standing" ("driver_id");

CREATE TABLE "season_constructor_standing"
( "year" INTEGER NOT NULL
, "position_display_order" INTEGER NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "points" DECIMAL(8,2) NOT NULL
, CONSTRAINT "sscs_pk" PRIMARY KEY ("year", "position_display_order")
, CONSTRAINT "sscs_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "sscs_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "sscs_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
);

CREATE INDEX "sscs_year_idx" ON "season_constructor_standing" ("year");
CREATE INDEX "sscs_position_display_order_idx" ON "season_constructor_standing" ("position_display_order");
CREATE INDEX "sscs_position_number_idx" ON "season_constructor_standing" ("position_number");
CREATE INDEX "sscs_position_text_idx" ON "season_constructor_standing" ("position_text");
CREATE INDEX "sscs_constructor_id_idx" ON "season_constructor_standing" ("constructor_id");
CREATE INDEX "sscs_engine_manufacturer_id_idx" ON "season_constructor_standing" ("engine_manufacturer_id");

CREATE TABLE "race"
( "id" INTEGER NOT NULL
, "year" INTEGER NOT NULL
, "round" INTEGER NOT NULL
, "date" DATE NOT NULL
, "time" TEXT
, "grand_prix_id" VARCHAR(100) NOT NULL
, "official_name" VARCHAR(100) NOT NULL
, "qualifying_format" VARCHAR(20) NOT NULL
, "sprint_qualifying_format" VARCHAR(20)
, "circuit_id" VARCHAR(100) NOT NULL
, "circuit_type" VARCHAR(6) NOT NULL
, "course_length" DECIMAL(6,3) NOT NULL
, "laps" INTEGER NOT NULL
, "distance" DECIMAL(6,3) NOT NULL
, "scheduled_laps" INTEGER
, "scheduled_distance" DECIMAL(6,3)
, "pre_qualifying_date" DATE
, "pre_qualifying_time" VARCHAR(5)
, "free_practice_1_date" DATE
, "free_practice_1_time" VARCHAR(5)
, "free_practice_2_date" DATE
, "free_practice_2_time" VARCHAR(5)
, "free_practice_3_date" DATE
, "free_practice_3_time" VARCHAR(5)
, "free_practice_4_date" DATE
, "free_practice_4_time" VARCHAR(5)
, "qualifying_1_date" DATE
, "qualifying_1_time" VARCHAR(5)
, "qualifying_2_date" DATE
, "qualifying_2_time" VARCHAR(5)
, "qualifying_date" DATE
, "qualifying_time" VARCHAR(5)
, "sprint_qualifying_date" DATE
, "sprint_qualifying_time" VARCHAR(5)
, "sprint_race_date" DATE
, "sprint_race_time" VARCHAR(5)
, "warming_up_date" DATE
, "warming_up_time" VARCHAR(5)
, CONSTRAINT "race_pk" PRIMARY KEY ("id")
, CONSTRAINT "race_year_round_uk" UNIQUE ("year", "round")
, CONSTRAINT "race_year_fk" FOREIGN KEY ("year") REFERENCES "season" ("year")
, CONSTRAINT "race_grand_prix_id_fk" FOREIGN KEY ("grand_prix_id") REFERENCES "grand_prix" ("id")
, CONSTRAINT "race_circuit_id_fk" FOREIGN KEY ("circuit_id") REFERENCES "circuit" ("id")
);

CREATE INDEX "race_year_idx" ON "race" ("year");
CREATE INDEX "race_round_idx" ON "race" ("round");
CREATE INDEX "race_date_idx" ON "race" ("date");
CREATE INDEX "race_grand_prix_id_idx" ON "race" ("grand_prix_id");
CREATE INDEX "race_official_name_idx" ON "race" ("official_name");
CREATE INDEX "race_circuit_id_idx" ON "race" ("circuit_id");

CREATE TABLE "race_data"
( "race_id" INTEGER NOT NULL
, "type" VARCHAR(50) NOT NULL
, "position_display_order" INTEGER NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4) NOT NULL
, "driver_number" VARCHAR(3) NOT NULL
, "driver_id" VARCHAR(100) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "tyre_manufacturer_id" VARCHAR(100) NOT NULL
, "practice_time" VARCHAR(20)
, "practice_time_millis" INTEGER
, "practice_gap" VARCHAR(20)
, "practice_gap_millis" INTEGER
, "practice_interval" VARCHAR(20)
, "practice_interval_millis" INTEGER
, "practice_laps" INTEGER
, "qualifying_time" VARCHAR(20)
, "qualifying_time_millis" INTEGER
, "qualifying_q1" VARCHAR(20)
, "qualifying_q1_millis" INTEGER
, "qualifying_q2" VARCHAR(20)
, "qualifying_q2_millis" INTEGER
, "qualifying_q3" VARCHAR(20)
, "qualifying_q3_millis" INTEGER
, "qualifying_gap" VARCHAR(20)
, "qualifying_gap_millis" INTEGER
, "qualifying_interval" VARCHAR(20)
, "qualifying_interval_millis" INTEGER
, "qualifying_laps" INTEGER
, "starting_grid_position_qualification_position_number" INTEGER
, "starting_grid_position_qualification_position_text" VARCHAR(4)
, "starting_grid_position_grid_penalty" VARCHAR(20)
, "starting_grid_position_grid_penalty_positions" INTEGER
, "starting_grid_position_time" VARCHAR(20)
, "starting_grid_position_time_millis" INTEGER
, "race_shared_car" BOOLEAN
, "race_laps" INTEGER
, "race_time" VARCHAR(20)
, "race_time_millis" INTEGER
, "race_time_penalty" VARCHAR(20)
, "race_time_penalty_millis" INTEGER
, "race_gap" VARCHAR(20)
, "race_gap_millis" INTEGER
, "race_gap_laps" INTEGER
, "race_interval" VARCHAR(20)
, "race_interval_millis" INTEGER
, "race_reason_retired" VARCHAR(100)
, "race_points" DECIMAL(8,2)
, "race_qualification_position_number" INTEGER
, "race_qualification_position_text" VARCHAR(4)
, "race_grid_position_number" INTEGER
, "race_grid_position_text" VARCHAR(2)
, "race_positions_gained" INTEGER
, "race_pit_stops" INTEGER
, "race_fastest_lap" BOOLEAN
, "race_driver_of_the_day" BOOLEAN
, "race_grand_slam" BOOLEAN
, "fastest_lap_lap" INTEGER
, "fastest_lap_time" VARCHAR(20)
, "fastest_lap_time_millis" INTEGER
, "fastest_lap_gap" VARCHAR(20)
, "fastest_lap_gap_millis" INTEGER
, "fastest_lap_interval" VARCHAR(20)
, "fastest_lap_interval_millis" INTEGER
, "pit_stop_stop" INTEGER
, "pit_stop_lap" INTEGER
, "pit_stop_time" VARCHAR(20)
, "pit_stop_time_millis" INTEGER
, "driver_of_the_day_percentage" DECIMAL(4,1)
, CONSTRAINT "rcda_pk" PRIMARY KEY ("race_id", "type", "position_display_order")
, CONSTRAINT "rcda_race_id_fk" FOREIGN KEY ("race_id") REFERENCES "race" ("id")
, CONSTRAINT "rcda_driver_id_fk" FOREIGN KEY ("driver_id") REFERENCES "driver" ("id")
, CONSTRAINT "rcda_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "rcda_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
, CONSTRAINT "rcda_tyre_manufacturer_id_fk" FOREIGN KEY ("tyre_manufacturer_id") REFERENCES "tyre_manufacturer" ("id")
);

CREATE INDEX "rcda_race_id_idx" ON "race_data" ("race_id");
CREATE INDEX "rcda_type_idx" ON "race_data" ("type");
CREATE INDEX "rcda_position_display_order_idx" ON "race_data" ("position_display_order");
CREATE INDEX "rcda_position_number_idx" ON "race_data" ("position_number");
CREATE INDEX "rcda_position_text_idx" ON "race_data" ("position_text");
CREATE INDEX "rcda_driver_number_idx" ON "race_data" ("driver_number");
CREATE INDEX "rcda_driver_id_idx" ON "race_data" ("driver_id");
CREATE INDEX "rcda_constructor_id_idx" ON "race_data" ("constructor_id");
CREATE INDEX "rcda_engine_manufacturer_id_idx" ON "race_data" ("engine_manufacturer_id");
CREATE INDEX "rcda_tyre_manufacturer_id_idx" ON "race_data" ("tyre_manufacturer_id");

CREATE TABLE "race_driver_standing"
( "race_id" INTEGER NOT NULL
, "position_display_order" INTEGER NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4) NOT NULL
, "driver_id" VARCHAR(100) NOT NULL
, "points" DECIMAL(8,2) NOT NULL
, "positions_gained" INTEGER
, CONSTRAINT "rcds_pk" PRIMARY KEY ("race_id", "position_display_order")
, CONSTRAINT "rcds_race_id_fk" FOREIGN KEY ("race_id") REFERENCES "race" ("id")
, CONSTRAINT "rcds_driver_id_fk" FOREIGN KEY ("driver_id") REFERENCES "driver" ("id")
);

CREATE INDEX "rcds_race_id_idx" ON "race_driver_standing" ("race_id");
CREATE INDEX "rcds_position_display_order_idx" ON "race_driver_standing" ("position_display_order");
CREATE INDEX "rcds_position_number_idx" ON "race_driver_standing" ("position_number");
CREATE INDEX "rcds_position_text_idx" ON "race_driver_standing" ("position_text");
CREATE INDEX "rcds_driver_id_idx" ON "race_driver_standing" ("driver_id");

CREATE TABLE "race_constructor_standing"
( "race_id" INTEGER NOT NULL
, "position_display_order" INTEGER NOT NULL
, "position_number" INTEGER
, "position_text" VARCHAR(4) NOT NULL
, "constructor_id" VARCHAR(100) NOT NULL
, "engine_manufacturer_id" VARCHAR(100) NOT NULL
, "points" DECIMAL(8,2) NOT NULL
, "positions_gained" INTEGER
, CONSTRAINT "rccs_pk" PRIMARY KEY ("race_id", "position_display_order")
, CONSTRAINT "rccs_race_id_fk" FOREIGN KEY ("race_id") REFERENCES "race" ("id")
, CONSTRAINT "rccs_constructor_id_fk" FOREIGN KEY ("constructor_id") REFERENCES "constructor" ("id")
, CONSTRAINT "rccs_engine_manufacturer_id_fk" FOREIGN KEY ("engine_manufacturer_id") REFERENCES "engine_manufacturer" ("id")
);

CREATE INDEX "rccs_race_id_idx" ON "race_constructor_standing" ("race_id");
CREATE INDEX "rccs_position_display_order_idx" ON "race_constructor_standing" ("position_display_order");
CREATE INDEX "rccs_position_number_idx" ON "race_constructor_standing" ("position_number");
CREATE INDEX "rccs_position_text_idx" ON "race_constructor_standing" ("position_text");
CREATE INDEX "rccs_constructor_id_idx" ON "race_constructor_standing" ("constructor_id");
CREATE INDEX "rccs_engine_manufacturer_id_idx" ON "race_constructor_standing" ("engine_manufacturer_id");

CREATE VIEW "pre_qualifying_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."qualifying_time" AS "time"
,        "race_data"."qualifying_time_millis" AS "time_millis"
,        "race_data"."qualifying_gap" AS "gap"
,        "race_data"."qualifying_gap_millis" AS "gap_millis"
,        "race_data"."qualifying_interval" AS "interval"
,        "race_data"."qualifying_interval_millis" AS "interval_millis"
,        "race_data"."qualifying_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'PRE_QUALIFYING_RESULT';

CREATE VIEW "free_practice_1_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."practice_time" AS "time"
,        "race_data"."practice_time_millis" AS "time_millis"
,        "race_data"."practice_gap" AS "gap"
,        "race_data"."practice_gap_millis" AS "gap_millis"
,        "race_data"."practice_interval" AS "interval"
,        "race_data"."practice_interval_millis" AS "interval_millis"
,        "race_data"."practice_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'FREE_PRACTICE_1_RESULT';

CREATE VIEW "free_practice_2_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."practice_time" AS "time"
,        "race_data"."practice_time_millis" AS "time_millis"
,        "race_data"."practice_gap" AS "gap"
,        "race_data"."practice_gap_millis" AS "gap_millis"
,        "race_data"."practice_interval" AS "interval"
,        "race_data"."practice_interval_millis" AS "interval_millis"
,        "race_data"."practice_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'FREE_PRACTICE_2_RESULT';

CREATE VIEW "free_practice_3_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."practice_time" AS "time"
,        "race_data"."practice_time_millis" AS "time_millis"
,        "race_data"."practice_gap" AS "gap"
,        "race_data"."practice_gap_millis" AS "gap_millis"
,        "race_data"."practice_interval" AS "interval"
,        "race_data"."practice_interval_millis" AS "interval_millis"
,        "race_data"."practice_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'FREE_PRACTICE_3_RESULT';

CREATE VIEW "free_practice_4_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."practice_time" AS "time"
,        "race_data"."practice_time_millis" AS "time_millis"
,        "race_data"."practice_gap" AS "gap"
,        "race_data"."practice_gap_millis" AS "gap_millis"
,        "race_data"."practice_interval" AS "interval"
,        "race_data"."practice_interval_millis" AS "interval_millis"
,        "race_data"."practice_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'FREE_PRACTICE_4_RESULT';

CREATE VIEW "qualifying_1_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."qualifying_time" AS "time"
,        "race_data"."qualifying_time_millis" AS "time_millis"
,        "race_data"."qualifying_gap" AS "gap"
,        "race_data"."qualifying_gap_millis" AS "gap_millis"
,        "race_data"."qualifying_interval" AS "interval"
,        "race_data"."qualifying_interval_millis" AS "interval_millis"
,        "race_data"."qualifying_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'QUALIFYING_1_RESULT';

CREATE VIEW "qualifying_2_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."qualifying_time" AS "time"
,        "race_data"."qualifying_time_millis" AS "time_millis"
,        "race_data"."qualifying_gap" AS "gap"
,        "race_data"."qualifying_gap_millis" AS "gap_millis"
,        "race_data"."qualifying_interval" AS "interval"
,        "race_data"."qualifying_interval_millis" AS "interval_millis"
,        "race_data"."qualifying_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'QUALIFYING_2_RESULT';

CREATE VIEW "qualifying_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."qualifying_time" AS "time"
,        "race_data"."qualifying_time_millis" AS "time_millis"
,        "race_data"."qualifying_q1" AS "q1"
,        "race_data"."qualifying_q1_millis" AS "q1_millis"
,        "race_data"."qualifying_q2" AS "q2"
,        "race_data"."qualifying_q2_millis" AS "q2_millis"
,        "race_data"."qualifying_q3" AS "q3"
,        "race_data"."qualifying_q3_millis" AS "q3_millis"
,        "race_data"."qualifying_gap" AS "gap"
,        "race_data"."qualifying_gap_millis" AS "gap_millis"
,        "race_data"."qualifying_interval" AS "interval"
,        "race_data"."qualifying_interval_millis" AS "interval_millis"
,        "race_data"."qualifying_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'QUALIFYING_RESULT';

CREATE VIEW "sprint_qualifying_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."qualifying_time" AS "time"
,        "race_data"."qualifying_time_millis" AS "time_millis"
,        "race_data"."qualifying_q1" AS "q1"
,        "race_data"."qualifying_q1_millis" AS "q1_millis"
,        "race_data"."qualifying_q2" AS "q2"
,        "race_data"."qualifying_q2_millis" AS "q2_millis"
,        "race_data"."qualifying_q3" AS "q3"
,        "race_data"."qualifying_q3_millis" AS "q3_millis"
,        "race_data"."qualifying_gap" AS "gap"
,        "race_data"."qualifying_gap_millis" AS "gap_millis"
,        "race_data"."qualifying_interval" AS "interval"
,        "race_data"."qualifying_interval_millis" AS "interval_millis"
,        "race_data"."qualifying_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'SPRINT_QUALIFYING_RESULT';

CREATE VIEW "sprint_starting_grid_position" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."starting_grid_position_qualification_position_number" AS "qualification_position_number"
,        "race_data"."starting_grid_position_qualification_position_text" AS "qualification_position_text"
,        "race_data"."starting_grid_position_grid_penalty" AS "grid_penalty"
,        "race_data"."starting_grid_position_grid_penalty_positions" AS "grid_penalty_positions"
,        "race_data"."starting_grid_position_time" AS "time"
,        "race_data"."starting_grid_position_time_millis" AS "time_millis"
FROM     "race_data"
WHERE    "race_data"."type" = 'SPRINT_STARTING_GRID_POSITION';

CREATE VIEW "sprint_race_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."race_laps" AS "laps"
,        "race_data"."race_time" AS "time"
,        "race_data"."race_time_millis" AS "time_millis"
,        "race_data"."race_time_penalty" AS "time_penalty"
,        "race_data"."race_time_penalty_millis" AS "time_penalty_millis"
,        "race_data"."race_gap" AS "gap"
,        "race_data"."race_gap_millis" AS "gap_millis"
,        "race_data"."race_gap_laps" AS "gap_laps"
,        "race_data"."race_interval" AS "interval"
,        "race_data"."race_interval_millis" AS "interval_millis"
,        "race_data"."race_reason_retired" AS "reason_retired"
,        "race_data"."race_points" AS "points"
,        "race_data"."race_qualification_position_number" AS "qualification_position_number"
,        "race_data"."race_qualification_position_text" AS "qualification_position_text"
,        "race_data"."race_grid_position_number" AS "grid_position_number"
,        "race_data"."race_grid_position_text" AS "grid_position_text"
,        "race_data"."race_positions_gained" AS "positions_gained"
FROM     "race_data"
WHERE    "race_data"."type" = 'SPRINT_RACE_RESULT';

CREATE VIEW "warming_up_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."practice_time" AS "time"
,        "race_data"."practice_time_millis" AS "time_millis"
,        "race_data"."practice_gap" AS "gap"
,        "race_data"."practice_gap_millis" AS "gap_millis"
,        "race_data"."practice_interval" AS "interval"
,        "race_data"."practice_interval_millis" AS "interval_millis"
,        "race_data"."practice_laps" AS "laps"
FROM     "race_data"
WHERE    "race_data"."type" = 'WARMING_UP_RESULT';

CREATE VIEW "starting_grid_position" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."starting_grid_position_qualification_position_number" AS "qualification_position_number"
,        "race_data"."starting_grid_position_qualification_position_text" AS "qualification_position_text"
,        "race_data"."starting_grid_position_grid_penalty" AS "grid_penalty"
,        "race_data"."starting_grid_position_grid_penalty_positions" AS "grid_penalty_positions"
,        "race_data"."starting_grid_position_time" AS "time"
,        "race_data"."starting_grid_position_time_millis" AS "time_millis"
FROM     "race_data"
WHERE    "race_data"."type" = 'STARTING_GRID_POSITION';

CREATE VIEW "race_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."race_shared_car" AS "shared_car"
,        "race_data"."race_laps" AS "laps"
,        "race_data"."race_time" AS "time"
,        "race_data"."race_time_millis" AS "time_millis"
,        "race_data"."race_time_penalty" AS "time_penalty"
,        "race_data"."race_time_penalty_millis" AS "time_penalty_millis"
,        "race_data"."race_gap" AS "gap"
,        "race_data"."race_gap_millis" AS "gap_millis"
,        "race_data"."race_gap_laps" AS "gap_laps"
,        "race_data"."race_interval" AS "interval"
,        "race_data"."race_interval_millis" AS "interval_millis"
,        "race_data"."race_reason_retired" AS "reason_retired"
,        "race_data"."race_points" AS "points"
,        "race_data"."race_qualification_position_number" AS "qualification_position_number"
,        "race_data"."race_qualification_position_text" AS "qualification_position_text"
,        "race_data"."race_grid_position_number" AS "grid_position_number"
,        "race_data"."race_grid_position_text" AS "grid_position_text"
,        "race_data"."race_positions_gained" AS "positions_gained"
,        "race_data"."race_pit_stops" AS "pit_stops"
,        "race_data"."race_fastest_lap" AS "fastest_lap"
,        "race_data"."race_driver_of_the_day" AS "driver_of_the_day"
,        "race_data"."race_grand_slam" AS "grand_slam"
FROM     "race_data"
WHERE    "race_data"."type" = 'RACE_RESULT';

CREATE VIEW "fastest_lap" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."fastest_lap_lap" AS "lap"
,        "race_data"."fastest_lap_time" AS "time"
,        "race_data"."fastest_lap_time_millis" AS "time_millis"
,        "race_data"."fastest_lap_gap" AS "gap"
,        "race_data"."fastest_lap_gap_millis" AS "gap_millis"
,        "race_data"."fastest_lap_interval" AS "interval"
,        "race_data"."fastest_lap_interval_millis" AS "interval_millis"
FROM     "race_data"
WHERE    "race_data"."type" = 'FASTEST_LAP';

CREATE VIEW "pit_stop" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."pit_stop_stop" AS "stop"
,        "race_data"."pit_stop_lap" AS "lap"
,        "race_data"."pit_stop_time" AS "time"
,        "race_data"."pit_stop_time_millis" AS "time_millis"
FROM     "race_data"
WHERE    "race_data"."type" = 'PIT_STOP';

CREATE VIEW "driver_of_the_day_result" AS
SELECT   "race_data"."race_id"
,        "race_data"."position_display_order"
,        "race_data"."position_number"
,        "race_data"."position_text"
,        "race_data"."driver_number"
,        "race_data"."driver_id"
,        "race_data"."constructor_id"
,        "race_data"."engine_manufacturer_id"
,        "race_data"."tyre_manufacturer_id"
,        "race_data"."driver_of_the_day_percentage" AS "percentage"
FROM     "race_data"
WHERE    "race_data"."type" = 'DRIVER_OF_THE_DAY_RESULT';
