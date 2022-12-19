package net.f1db.gradle.plugin

enum F1DBFileName {

    F1DB_FILE_NAME("f1db"),
    CONTINENTS_FILE_NAME("f1db-continents"),
    COUNTRIES_FILE_NAME("f1db-countries"),
    DRIVERS_FILE_NAME("f1db-drivers"),
    DRIVERS_FAMILY_RELATIONSHIPS_FILE_NAME("f1db-drivers-family-relationships"),
    CONSTRUCTORS_FILE_NAME("f1db-constructors"),
    CONSTRUCTORS_PREVIOUS_NEXT_CONSTRUCTORS_FILE_NAME("f1db-constructors-previous-next-constructors"),
    ENGINE_MANUFACTURERS_FILE_NAME("f1db-engine-manufacturers"),
    TYRE_MANUFACTURERS_FILE_NAME("f1db-tyre-manufacturers"),
    ENTRANTS_FILE_NAME("f1db-entrants"),
    CIRCUITS_FILE_NAME("f1db-circuits"),
    GRANDS_PRIX_FILE_NAME("f1db-grands-prix"),
    SEASONS_FILE_NAME("f1db-seasons"),
    SEASONS_ENTRANTS_FILE_NAME("f1db-seasons-entrants"),
    SEASONS_ENTRANTS_CONSTRUCTORS_FILE_NAME("f1db-seasons-entrants-constructors"),
    SEASONS_ENTRANTS_TYRE_MANUFACTURERS_FILE_NAME("f1db-seasons-entrants-tyre-manufacturers"),
    SEASONS_ENTRANTS_DRIVERS_FILE_NAME("f1db-seasons-entrants-drivers"),
    SEASONS_DRIVER_STANDINGS_FILE_NAME("f1db-seasons-driver-standings"),
    SEASONS_CONSTRUCTOR_STANDINGS_FILE_NAME("f1db-seasons-constructor-standings"),
    RACES_FILE_NAME("f1db-races"),
    RACES_PRE_QUALIFYING_RESULTS_FILE_NAME("f1db-races-pre-qualifying-results"),
    RACES_FREE_PRACTICE_1_RESULTS_FILE_NAME("f1db-races-free-practice-1-results"),
    RACES_FREE_PRACTICE_2_RESULTS_FILE_NAME("f1db-races-free-practice-2-results"),
    RACES_FREE_PRACTICE_3_RESULTS_FILE_NAME("f1db-races-free-practice-3-results"),
    RACES_FREE_PRACTICE_4_RESULTS_FILE_NAME("f1db-races-free-practice-4-results"),
    RACES_QUALIFYING_1_RESULTS_FILE_NAME("f1db-races-qualifying-1-results"),
    RACES_QUALIFYING_2_RESULTS_FILE_NAME("f1db-races-qualifying-2-results"),
    RACES_QUALIFYING_RESULTS_FILE_NAME("f1db-races-qualifying-results"),
    RACES_SPRINT_QUALIFYING_STARTING_GRID_POSITIONS_FILE_NAME("f1db-races-sprint-qualifying-starting-grid-positions"),
    RACES_SPRINT_QUALIFYING_RESULTS_FILE_NAME("f1db-races-sprint-qualifying-results"),
    RACES_WARMING_UP_RESULTS_FILE_NAME("f1db-races-warming-up-results"),
    RACES_STARTING_GRID_POSITIONS_FILE_NAME("f1db-races-starting-grid-positions"),
    RACES_RACE_RESULTS_FILE_NAME("f1db-races-race-results"),
    RACES_FASTEST_LAPS_FILE_NAME("f1db-races-fastest-laps"),
    RACES_PIT_STOPS_FILE_NAME("f1db-races-pit-stops"),
    RACES_DRIVER_OF_THE_DAY_RESULTS_FILE_NAME("f1db-races-driver-of-the-day-results"),
    RACES_DRIVER_STANDINGS_FILE_NAME("f1db-races-driver-standings"),
    RACES_CONSTRUCTOR_STANDINGS_FILE_NAME("f1db-races-constructor-standings")

    String fileName

    F1DBFileName(String fileName) {
        this.fileName = fileName
    }

    String csv() {
        return fileName + ".csv"
    }

    String json() {
        return fileName + ".json"
    }

    String smile() {
        return fileName + ".sml"
    }
}
