package com.f1db.plugin.writer.sqlite

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.RaceDataType
import com.f1db.plugin.schema.single.F1db
import com.f1db.plugin.writer.sql.Tables.CIRCUIT
import com.f1db.plugin.writer.sql.Tables.CONSTRUCTOR
import com.f1db.plugin.writer.sql.Tables.CONSTRUCTOR_PREVIOUS_NEXT_CONSTRUCTOR
import com.f1db.plugin.writer.sql.Tables.CONTINENT
import com.f1db.plugin.writer.sql.Tables.COUNTRY
import com.f1db.plugin.writer.sql.Tables.DRIVER
import com.f1db.plugin.writer.sql.Tables.DRIVER_FAMILY_RELATIONSHIP
import com.f1db.plugin.writer.sql.Tables.ENGINE_MANUFACTURER
import com.f1db.plugin.writer.sql.Tables.ENTRANT
import com.f1db.plugin.writer.sql.Tables.GRAND_PRIX
import com.f1db.plugin.writer.sql.Tables.RACE
import com.f1db.plugin.writer.sql.Tables.RACE_CONSTRUCTOR_STANDING
import com.f1db.plugin.writer.sql.Tables.RACE_DATA
import com.f1db.plugin.writer.sql.Tables.RACE_DRIVER_STANDING
import com.f1db.plugin.writer.sql.Tables.SEASON
import com.f1db.plugin.writer.sql.Tables.SEASON_CONSTRUCTOR_STANDING
import com.f1db.plugin.writer.sql.Tables.SEASON_DRIVER_STANDING
import com.f1db.plugin.writer.sql.Tables.SEASON_ENTRANT
import com.f1db.plugin.writer.sql.Tables.SEASON_ENTRANT_CONSTRUCTOR
import com.f1db.plugin.writer.sql.Tables.SEASON_ENTRANT_DRIVER
import com.f1db.plugin.writer.sql.Tables.SEASON_ENTRANT_TYRE_MANUFACTURER
import com.f1db.plugin.writer.sql.Tables.TYRE_MANUFACTURER
import com.f1db.plugin.writer.sql.mapper.circuitMapper
import com.f1db.plugin.writer.sql.mapper.constructorMapper
import com.f1db.plugin.writer.sql.mapper.constructorPreviousNextConstructorMapper
import com.f1db.plugin.writer.sql.mapper.continentMapper
import com.f1db.plugin.writer.sql.mapper.countryMapper
import com.f1db.plugin.writer.sql.mapper.driverFamilyRelationshipMapper
import com.f1db.plugin.writer.sql.mapper.driverMapper
import com.f1db.plugin.writer.sql.mapper.engineManufacturerMapper
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
import com.f1db.plugin.writer.sql.mapper.seasonConstructorStandingMapper
import com.f1db.plugin.writer.sql.mapper.seasonDriverStandingMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantConstructorMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantDriverMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantMapper
import com.f1db.plugin.writer.sql.mapper.seasonEntrantTyreManufacturerMapper
import com.f1db.plugin.writer.sql.mapper.seasonMapper
import com.f1db.plugin.writer.sql.mapper.tyreManufacturerMapper
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.Table
import org.jooq.impl.DSL
import java.io.File
import java.sql.DriverManager
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
            batchInsert(ctx, CONSTRUCTOR_PREVIOUS_NEXT_CONSTRUCTOR, constructorPreviousNextConstructorMapper.unmap(db.splitted.constructorPreviousNextConstructors))
            batchInsert(ctx, ENGINE_MANUFACTURER, engineManufacturerMapper.unmap(db.splitted.engineManufacturers))
            batchInsert(ctx, TYRE_MANUFACTURER, tyreManufacturerMapper.unmap(db.splitted.tyreManufacturers))
            batchInsert(ctx, ENTRANT, entrantMapper.unmap(db.splitted.entrants))
            batchInsert(ctx, CIRCUIT, circuitMapper.unmap(db.splitted.circuits))
            batchInsert(ctx, GRAND_PRIX, grandPrixMapper.unmap(db.splitted.grandsPrix))
            batchInsert(ctx, SEASON, seasonMapper.unmap(db.splitted.seasons))
            batchInsert(ctx, SEASON_ENTRANT, seasonEntrantMapper.unmap(db.splitted.seasonEntrants))
            batchInsert(ctx, SEASON_ENTRANT_CONSTRUCTOR, seasonEntrantConstructorMapper.unmap(db.splitted.seasonEntrantConstructors))
            batchInsert(ctx, SEASON_ENTRANT_TYRE_MANUFACTURER, seasonEntrantTyreManufacturerMapper.unmap(db.splitted.seasonEntrantTyreManufacturers))
            batchInsert(ctx, SEASON_ENTRANT_DRIVER, seasonEntrantDriverMapper.unmap(db.splitted.seasonEntrantDrivers))
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
    }

    private fun <R : Record> batchInsert(ctx: DSLContext, table: Table<R>, records: List<R>, batchSize: Int = 1000) {
        records.chunked(batchSize).forEach { batch -> ctx.insertInto(table).set(batch).execute() }
    }
}
