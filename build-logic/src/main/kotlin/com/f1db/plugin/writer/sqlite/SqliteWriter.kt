package com.f1db.plugin.writer.sqlite

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.RaceDataType
import com.f1db.plugin.schema.single.F1db
import com.f1db.plugin.writer.sql.Tables.*
import com.f1db.plugin.writer.sql.mapper.chassisMapper
import com.f1db.plugin.writer.sql.mapper.circuitMapper
import com.f1db.plugin.writer.sql.mapper.constructorChronologyMapper
import com.f1db.plugin.writer.sql.mapper.constructorMapper
import com.f1db.plugin.writer.sql.mapper.continentMapper
import com.f1db.plugin.writer.sql.mapper.countryMapper
import com.f1db.plugin.writer.sql.mapper.driverFamilyRelationshipMapper
import com.f1db.plugin.writer.sql.mapper.driverMapper
import com.f1db.plugin.writer.sql.mapper.engineManufacturerMapper
import com.f1db.plugin.writer.sql.mapper.engineMapper
import com.f1db.plugin.writer.sql.mapper.entrantMapper
import com.f1db.plugin.writer.sql.mapper.grandPrixMapper
import com.f1db.plugin.writer.sql.mapper.raceConstructorStandingMapper
import com.f1db.plugin.writer.sql.mapper.raceDriverOfTheDayResultMapper
import com.f1db.plugin.writer.sql.mapper.raceDriverStandingMapper
import com.f1db.plugin.writer.sql.mapper.raceFastestLapMapper
import com.f1db.plugin.writer.sql.mapper.raceMapper
import com.f1db.plugin.writer.sql.mapper.racePitStopMapper
import com.f1db.plugin.writer.sql.mapper.racePracticeResultMapper
import com.f1db.plugin.writer.sql.mapper.raceQualifyingResultMapper
import com.f1db.plugin.writer.sql.mapper.raceRaceResultMapper
import com.f1db.plugin.writer.sql.mapper.raceStartingGridPositionMapper
import com.f1db.plugin.writer.sql.mapper.seasonConstructorMapper
import com.f1db.plugin.writer.sql.mapper.seasonConstructorStandingMapper
import com.f1db.plugin.writer.sql.mapper.seasonDriverMapper
import com.f1db.plugin.writer.sql.mapper.seasonDriverStandingMapper
import com.f1db.plugin.writer.sql.mapper.seasonEngineManufacturerMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantChassisMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantConstructorMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantDriverMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantEngineMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantTyreManufacturerMapper
import com.f1db.plugin.writer.sql.mapper.seasonMapper
import com.f1db.plugin.writer.sql.mapper.seasonTyreManufacturerMapper
import com.f1db.plugin.writer.sql.mapper.tyreManufacturerMapper
import com.f1db.plugin.writer.sql.tables.records.DriverRecord
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.Table
import org.jooq.impl.DSL
import java.io.File
import java.math.BigDecimal
import java.sql.DriverManager
import java.time.LocalDate
import java.util.*

/**
 * The SQLite writer.
 *
 * @author Marcel Overdijk
 */
class SqliteWriter(
        private val projectName: String,
        private val outputDir: File,
        private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        val outputFile = File(outputDir, "$projectName.db")

        println("Writing ${outputFile.name}....")

        // Connection properties.

        val jdbcUrl = "jdbc:sqlite:${outputFile.absolutePath}"
        val jdbcProperties = Properties().apply {
            setProperty("date_class", "text")
            setProperty("date_string_format", "yyyy-MM-dd")
        }

        Class.forName("org.sqlite.JDBC")

        DriverManager.getConnection(jdbcUrl, jdbcProperties).use { connection ->

            val ctx = DSL.using(connection, SQLDialect.SQLITE)

            // Enable foreign keys

            ctx.execute("PRAGMA foreign_keys = ON;")

            // Create schema.

            val ddl = this::class.java.getResource("/sql/create_schema.sql")!!.readText()
            ddl.split(";").forEach { statement ->
                val trimmedStatement = statement.trim()
                if (trimmedStatement.isNotEmpty()) {
                    ctx.execute(trimmedStatement)
                }
            }

            // Insert data.

            batchInsert(ctx, CONTINENT, continentMapper.unmap(db.splitted.continents))
            batchInsert(ctx, COUNTRY, countryMapper.unmap(db.splitted.countries))
            batchInsert(ctx, DRIVER, driverMapper.unmap(db.splitted.drivers))
            batchInsert(ctx, DRIVER_FAMILY_RELATIONSHIP, driverFamilyRelationshipMapper.unmap(db.splitted.driverFamilyRelationships))
            batchInsert(ctx, CONSTRUCTOR, constructorMapper.unmap(db.splitted.constructors))
            batchInsert(ctx, CONSTRUCTOR_CHRONOLOGY, constructorChronologyMapper.unmap(db.splitted.constructorChronology))
            batchInsert(ctx, CHASSIS, chassisMapper.unmap(db.splitted.chassis))
            batchInsert(ctx, ENGINE_MANUFACTURER, engineManufacturerMapper.unmap(db.splitted.engineManufacturers))
            batchInsert(ctx, ENGINE, engineMapper.unmap(db.splitted.engines))
            batchInsert(ctx, TYRE_MANUFACTURER, tyreManufacturerMapper.unmap(db.splitted.tyreManufacturers))
            batchInsert(ctx, ENTRANT, entrantMapper.unmap(db.splitted.entrants))
            batchInsert(ctx, CIRCUIT, circuitMapper.unmap(db.splitted.circuits))
            batchInsert(ctx, GRAND_PRIX, grandPrixMapper.unmap(db.splitted.grandsPrix))
            batchInsert(ctx, SEASON, seasonMapper.unmap(db.splitted.seasons))
            batchInsert(ctx, SEASON_ENTRANT, seasonEntrantMapper.unmap(db.splitted.seasonEntrants))
            batchInsert(ctx, SEASON_ENTRANT_CONSTRUCTOR, seasonEntrantConstructorMapper.unmap(db.splitted.seasonEntrantConstructors))
            batchInsert(ctx, SEASON_ENTRANT_CHASSIS, seasonEntrantChassisMapper.unmap(db.splitted.seasonEntrantChassis))
            batchInsert(ctx, SEASON_ENTRANT_ENGINE, seasonEntrantEngineMapper.unmap(db.splitted.seasonEntrantEngines))
            batchInsert(ctx, SEASON_ENTRANT_TYRE_MANUFACTURER, seasonEntrantTyreManufacturerMapper.unmap(db.splitted.seasonEntrantTyreManufacturers))
            batchInsert(ctx, SEASON_ENTRANT_DRIVER, seasonEntrantDriverMapper.unmap(db.splitted.seasonEntrantDrivers))
            batchInsert(ctx, SEASON_CONSTRUCTOR, seasonConstructorMapper.unmap(db.splitted.seasonConstructors))
            batchInsert(ctx, SEASON_ENGINE_MANUFACTURER, seasonEngineManufacturerMapper.unmap(db.splitted.seasonEngineManufacturers))
            batchInsert(ctx, SEASON_TYRE_MANUFACTURER, seasonTyreManufacturerMapper.unmap(db.splitted.seasonTyreManufacturers))
            batchInsert(ctx, SEASON_DRIVER, seasonDriverMapper.unmap(db.splitted.seasonDrivers))
            batchInsert(ctx, SEASON_DRIVER_STANDING, seasonDriverStandingMapper.unmap(db.splitted.seasonDriverStandings))
            batchInsert(ctx, SEASON_CONSTRUCTOR_STANDING, seasonConstructorStandingMapper.unmap(db.splitted.seasonConstructorStandings))
            batchInsert(ctx, RACE, raceMapper.unmap(db.splitted.races))
            batchInsert(ctx, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.racePreQualifyingResults, RaceDataType.PRE_QUALIFYING_RESULT))
            batchInsert(ctx, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice1Results, RaceDataType.FREE_PRACTICE_1_RESULT))
            batchInsert(ctx, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice2Results, RaceDataType.FREE_PRACTICE_2_RESULT))
            batchInsert(ctx, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice3Results, RaceDataType.FREE_PRACTICE_3_RESULT))
            batchInsert(ctx, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice4Results, RaceDataType.FREE_PRACTICE_4_RESULT))
            batchInsert(ctx, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceQualifying1Results, RaceDataType.QUALIFYING_1_RESULT))
            batchInsert(ctx, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceQualifying2Results, RaceDataType.QUALIFYING_2_RESULT))
            batchInsert(ctx, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceQualifyingResults, RaceDataType.QUALIFYING_RESULT))
            batchInsert(ctx, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceSprintQualifyingResults, RaceDataType.SPRINT_QUALIFYING_RESULT))
            batchInsert(ctx, RACE_DATA, raceStartingGridPositionMapper.unmap(db.splitted.raceSprintStartingGridPositions, RaceDataType.SPRINT_STARTING_GRID_POSITION))
            batchInsert(ctx, RACE_DATA, raceRaceResultMapper.unmap(db.splitted.raceSprintRaceResults, RaceDataType.SPRINT_RACE_RESULT))
            batchInsert(ctx, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceWarmingUpResults, RaceDataType.WARMING_UP_RESULT))
            batchInsert(ctx, RACE_DATA, raceStartingGridPositionMapper.unmap(db.splitted.raceStartingGridPositions, RaceDataType.STARTING_GRID_POSITION))
            batchInsert(ctx, RACE_DATA, raceRaceResultMapper.unmap(db.splitted.raceRaceResults, RaceDataType.RACE_RESULT))
            batchInsert(ctx, RACE_DATA, raceFastestLapMapper.unmap(db.splitted.raceFastestLaps, RaceDataType.FASTEST_LAP))
            batchInsert(ctx, RACE_DATA, racePitStopMapper.unmap(db.splitted.racePitStops, RaceDataType.PIT_STOP))
            batchInsert(ctx, RACE_DATA, raceDriverOfTheDayResultMapper.unmap(db.splitted.raceDriverOfTheDayResults, RaceDataType.DRIVER_OF_THE_DAY_RESULT))
            batchInsert(ctx, RACE_DRIVER_STANDING, raceDriverStandingMapper.unmap(db.splitted.raceDriverStandings))
            batchInsert(ctx, RACE_CONSTRUCTOR_STANDING, raceConstructorStandingMapper.unmap(db.splitted.raceConstructorStandings))

            // Rebuild the database; repacking it into a minimal amount of disk space.

            ctx.execute("VACUUM;")
        }

        // Validate the SQLite database file.

        DriverManager.getConnection(jdbcUrl, jdbcProperties).use { connection ->

            val ctx = DSL.using(connection, SQLDialect.SQLITE)

            // Validate tables.

            val expectedTables = mapOf(
                "driver" to 900..1250,
                "driver_family_relationship" to 75..125,
                "constructor" to 175..225,
                "constructor_chronology" to 200..300,
                "chassis" to 1000..2000,
                "engine_manufacturer" to 75..100,
                "engine" to 400..500,
                "tyre_manufacturer" to 9..10,
                "entrant" to 800..1000,
                "circuit" to 75..100,
                "grand_prix" to 50..75,
                "season" to 75..100,
                "season_entrant" to 1500..3000,
                "season_entrant_constructor" to 1500..3000,
                "season_entrant_engine" to 1500..3000,
                "season_entrant_chassis" to 1500..3000,
                "season_entrant_tyre_manufacturer" to 1500..3000,
                "season_entrant_driver" to 3500..5000,
                "season_constructor" to 1000..1500,
                "season_engine_manufacturer" to 500..1000,
                "season_driver" to 3000..4000,
                "season_driver_standing" to 1500..2500,
                "season_constructor_standing" to 500..1000,
                "race" to 1000..1500,
                "race_data" to 175000..250000,
                "race_driver_standing" to 20000..30000,
                "race_constructor_standing" to 10000..15000,
                "continent" to 7..7,
                "country" to 249..249,
            )

            val actualTables = connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'table'").use { rs ->
                    generateSequence { if (rs.next()) rs.getString("name") else null }.toSet()
                }
            }

            // Validate no missing tables.

            require(expectedTables.keys.all { it in actualTables }) { "SqliteWriter > Missing tables: ${expectedTables.keys - actualTables}" }

            // Validate estimated record count for tables.

            expectedTables.forEach { (name, range) ->
                val count = connection.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT COUNT(*) FROM $name").use { rs ->
                        if (rs.next()) rs.getInt(1) else 0
                    }
                }
                require(count in range) { "SqliteWriter > Table '$name' has unexpected record count, expected $range but found $count" }
            }

            // Validate views.

            val expectedViews = mapOf(
                "pre_qualifying_result" to 600..700,
                "free_practice_1_result" to 15000..20000,
                "free_practice_2_result" to 12500..20000,
                "free_practice_3_result" to 7500..15000,
                "free_practice_4_result" to 500..1000,
                "qualifying_1_result" to 5000..10000,
                "qualifying_2_result" to 5000..10000,
                "qualifying_result" to 25000..30000,
                "sprint_qualifying_result" to 200..2000,
                "sprint_starting_grid_position" to 200..2000,
                "sprint_race_result" to 200..2000,
                "warming_up_result" to 7500..10000,
                "starting_grid_position" to 25000..35000,
                "race_result" to 25000..35000,
                "fastest_lap" to 15000..25000,
                "pit_stop" to 20000..30000,
                "driver_of_the_day_result" to 500..5000
            )

            val actualViews = connection.createStatement().use { stmt ->
                stmt.executeQuery("SELECT name FROM sqlite_master WHERE type = 'view'").use { rs ->
                    generateSequence { if (rs.next()) rs.getString("name") else null }.toSet()
                }
            }

            // Validate no missing views.

            require(expectedViews.keys.all { it in actualViews }) { "SqliteWriter > Missing views: ${expectedViews.keys - actualViews}" }

            // Validate estimated record count for views.

            expectedViews.forEach { (name, range) ->
                val count = connection.createStatement().use { stmt ->
                    stmt.executeQuery("SELECT COUNT(*) FROM $name").use { rs ->
                        if (rs.next()) rs.getInt(1) else 0
                    }
                }
                require(count in range) { "SqliteWriter > View '$name' has unexpected record count, expected $range but found $count" }
            }

            // Validate data (Ayrton Senna).

            val data = ctx.selectFrom(DRIVER).where(DRIVER.ID.eq("ayrton-senna")).fetchOne()

            requireNotNull(data) { "SqliteWriter > Missing driver: Ayrton Senna" }
            require(data.fullName == "Ayrton Senna da Silva") { "SqliteWriter > Ayrton Senna full name mismatch, expected 'Ayrton Senna da Silva' but found '${data.fullName}'" }
            require(data.abbreviation == "SEN") { "SqliteWriter > Ayrton Senna abbreviation mismatch, expected 'SEN' but found '${data.abbreviation}'" }
            require(data.dateOfBirth == LocalDate.of(1960, 3, 21)) { "SqliteWriter > Ayrton Senna date of birth mismatch, expected '1960-03-21' but found '${data.dateOfBirth}'" }
            require(data.dateOfDeath == LocalDate.of(1994, 5, 1)) { "SqliteWriter > Ayrton Senna date of death mismatch, expected '1994-05-01' but found '${data.dateOfDeath}'" }
            require(data.nationalityCountryId == "brazil") { "SqliteWriter > Ayrton Senna nationality mismatch, expected 'brazil' but found '${data.nationalityCountryId}'" }
            require(data.totalChampionshipWins == 3) { "SqliteWriter > Ayrton Senna total Championship wins mismatch, expected 3 but found ${data.totalChampionshipWins}" }
            require(data.totalRaceEntries == 162) { "SqliteWriter > Ayrton Senna total race entries mismatch, expected 162 but found ${data.totalRaceEntries}" }
            require(data.totalRaceStarts == 161) { "SqliteWriter > Ayrton Senna total race starts mismatch, expected 161 but found ${data.totalRaceStarts}" }
            require(data.totalRaceWins == 41) { "SqliteWriter > Ayrton Senna total race wins mismatch, expected 41 but found ${data.totalRaceWins}" }
            require(data.totalRaceLaps == 8219) { "SqliteWriter > Ayrton Senna total race laps mismatch, expected 8219 but found ${data.totalRaceLaps}" }
            require(data.totalPodiums == 80) { "SqliteWriter > Ayrton Senna total podiums mismatch, expected 80 but found ${data.totalPodiums}" }
            require(data.totalPoints == BigDecimal(614)) { "SqliteWriter > Ayrton Senna total points mismatch, expected 614 but found ${data.totalPoints}" }
            require(data.totalChampionshipPoints == BigDecimal(610)) { "SqliteWriter > Ayrton Senna total Championship points mismatch, expected 610 but found ${data.totalChampionshipPoints}" }
            require(data.totalPolePositions == 65) { "SqliteWriter > Ayrton Senna total pole positions mismatch, expected 65 but found ${data.totalPolePositions}" }
            require(data.totalFastestLaps == 19) { "SqliteWriter > Ayrton Senna total fastest laps mismatch, expected 19 but found ${data.totalFastestLaps}" }
            require(data.totalDriverOfTheDay == 0) { "SqliteWriter > Ayrton Senna total Driver of the Day mismatch, expected 0 but found ${data.totalDriverOfTheDay}" }
            require(data.totalGrandSlams == 4) { "SqliteWriter > Ayrton Senna total Grand Slams mismatch, expected 4 but found ${data.totalGrandSlams}" }
        }
    }

    private fun <R : Record> batchInsert(ctx: DSLContext, table: Table<R>, records: List<R>, batchSize: Int = 1000) {
        records.chunked(batchSize).forEach { batch -> ctx.insertInto(table).set(batch).execute() }
    }
}
