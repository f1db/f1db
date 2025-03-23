package com.f1db.plugin.writer.sql

import com.f1db.plugin.writer.sql.Tables.*
import org.jooq.impl.TableImpl

fun getAllTables(): List<TableImpl<*>> {
    return listOf(
        CONTINENT,
        COUNTRY,
        DRIVER,
        DRIVER_FAMILY_RELATIONSHIP,
        CONSTRUCTOR,
        CONSTRUCTOR_CHRONOLOGY,
        CHASSIS,
        ENGINE_MANUFACTURER,
        ENGINE,
        TYRE_MANUFACTURER,
        ENTRANT,
        CIRCUIT,
        GRAND_PRIX,
        SEASON,
        SEASON_ENTRANT,
        SEASON_ENTRANT_CONSTRUCTOR,
        SEASON_ENTRANT_CHASSIS,
        SEASON_ENTRANT_ENGINE,
        SEASON_ENTRANT_TYRE_MANUFACTURER,
        SEASON_ENTRANT_DRIVER,
        SEASON_CONSTRUCTOR,
        SEASON_ENGINE_MANUFACTURER,
        SEASON_TYRE_MANUFACTURER,
        SEASON_DRIVER,
        SEASON_DRIVER_STANDING,
        SEASON_CONSTRUCTOR_STANDING,
        RACE,
        RACE_DATA,
        RACE_DRIVER_STANDING,
        RACE_CONSTRUCTOR_STANDING
    )
}

fun getAllViews(): List<TableImpl<*>> {
    return listOf(
        PRE_QUALIFYING_RESULT,
        FREE_PRACTICE_1_RESULT,
        FREE_PRACTICE_2_RESULT,
        FREE_PRACTICE_3_RESULT,
        FREE_PRACTICE_4_RESULT,
        QUALIFYING_1_RESULT,
        QUALIFYING_2_RESULT,
        QUALIFYING_RESULT,
        SPRINT_QUALIFYING_RESULT,
        SPRINT_STARTING_GRID_POSITION,
        SPRINT_RACE_RESULT,
        WARMING_UP_RESULT,
        STARTING_GRID_POSITION,
        RACE_RESULT,
        FASTEST_LAP,
        PIT_STOP,
        DRIVER_OF_THE_DAY_RESULT
    )
}
