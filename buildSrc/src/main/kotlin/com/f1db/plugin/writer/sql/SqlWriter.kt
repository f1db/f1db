package com.f1db.plugin.writer.sql

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
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SQLDialect
import org.jooq.Table
import org.jooq.conf.ParamType
import org.jooq.conf.RenderKeywordCase
import org.jooq.conf.Settings
import org.jooq.impl.DSL
import org.jooq.impl.DSL.*
import java.io.File
import java.io.PrintWriter

/**
 * The SQL writer.
 *
 * @author Marcel Overdijk
 */
class SqlWriter(
        private val projectName: String,
        private val outputDir: File,
        private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        write("mysql", SQLDialect.MYSQL)
        write("postgresql", SQLDialect.POSTGRES)
        write("sqlite", SQLDialect.SQLITE)
    }

    private fun write(name: String, dialect: SQLDialect) {

        val outputFile = File(outputDir, "$projectName-sql-$name.sql")

        println("Writing ${outputFile.name}....")

        val settings = Settings()
                .withParamType(ParamType.INLINED)
                .withRenderKeywordCase(RenderKeywordCase.UPPER)
                .withRenderFormatted(false)
                .withRenderSchema(false)

        val ctx = DSL.using(dialect, settings)

        outputFile.printWriter().use { out ->

            // Write drop schema statements.

            writeDropViews(ctx, out)
            writeDropTables(ctx, out)

            // Write create schema statements.

            writeCreateSchema(ctx, out)
            writeCreateViews(ctx, out)

            // Write insert statements.

            writeInserts(ctx, out, CONTINENT, continentMapper.unmap(db.splitted.continents))
            writeInserts(ctx, out, COUNTRY, countryMapper.unmap(db.splitted.countries))
            writeInserts(ctx, out, DRIVER, driverMapper.unmap(db.splitted.drivers))
            writeInserts(ctx, out, DRIVER_FAMILY_RELATIONSHIP, driverFamilyRelationshipMapper.unmap(db.splitted.driverFamilyRelationships))
            writeInserts(ctx, out, CONSTRUCTOR, constructorMapper.unmap(db.splitted.constructors))
            writeInserts(ctx, out, CONSTRUCTOR_CHRONOLOGY, constructorChronologyMapper.unmap(db.splitted.constructorChronology))
            writeInserts(ctx, out, CHASSIS, chassisMapper.unmap(db.splitted.chassis))
            writeInserts(ctx, out, ENGINE_MANUFACTURER, engineManufacturerMapper.unmap(db.splitted.engineManufacturers))
            writeInserts(ctx, out, ENGINE, engineMapper.unmap(db.splitted.engines))
            writeInserts(ctx, out, TYRE_MANUFACTURER, tyreManufacturerMapper.unmap(db.splitted.tyreManufacturers))
            writeInserts(ctx, out, ENTRANT, entrantMapper.unmap(db.splitted.entrants))
            writeInserts(ctx, out, CIRCUIT, circuitMapper.unmap(db.splitted.circuits))
            writeInserts(ctx, out, GRAND_PRIX, grandPrixMapper.unmap(db.splitted.grandsPrix))
            writeInserts(ctx, out, SEASON, seasonMapper.unmap(db.splitted.seasons))
            writeInserts(ctx, out, SEASON_ENTRANT, seasonEntrantMapper.unmap(db.splitted.seasonEntrants))
            writeInserts(ctx, out, SEASON_ENTRANT_CONSTRUCTOR, seasonEntrantConstructorMapper.unmap(db.splitted.seasonEntrantConstructors))
            writeInserts(ctx, out, SEASON_ENTRANT_CHASSIS, seasonEntrantChassisMapper.unmap(db.splitted.seasonEntrantChassis))
            writeInserts(ctx, out, SEASON_ENTRANT_ENGINE, seasonEntrantEngineMapper.unmap(db.splitted.seasonEntrantEngines))
            writeInserts(ctx, out, SEASON_ENTRANT_TYRE_MANUFACTURER, seasonEntrantTyreManufacturerMapper.unmap(db.splitted.seasonEntrantTyreManufacturers))
            writeInserts(ctx, out, SEASON_ENTRANT_DRIVER, seasonEntrantDriverMapper.unmap(db.splitted.seasonEntrantDrivers))
            writeInserts(ctx, out, SEASON_CONSTRUCTOR, seasonConstructorMapper.unmap(db.splitted.seasonConstructors))
            writeInserts(ctx, out, SEASON_ENGINE_MANUFACTURER, seasonEngineManufacturerMapper.unmap(db.splitted.seasonEngineManufacturers))
            writeInserts(ctx, out, SEASON_TYRE_MANUFACTURER, seasonTyreManufacturerMapper.unmap(db.splitted.seasonTyreManufacturers))
            writeInserts(ctx, out, SEASON_DRIVER, seasonDriverMapper.unmap(db.splitted.seasonDrivers))
            writeInserts(ctx, out, SEASON_DRIVER_STANDING, seasonDriverStandingMapper.unmap(db.splitted.seasonDriverStandings))
            writeInserts(ctx, out, SEASON_CONSTRUCTOR_STANDING, seasonConstructorStandingMapper.unmap(db.splitted.seasonConstructorStandings))
            writeInserts(ctx, out, RACE, raceMapper.unmap(db.splitted.races))
            writeInserts(ctx, out, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.racePreQualifyingResults, RaceDataType.PRE_QUALIFYING_RESULT))
            writeInserts(ctx, out, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice1Results, RaceDataType.FREE_PRACTICE_1_RESULT))
            writeInserts(ctx, out, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice2Results, RaceDataType.FREE_PRACTICE_2_RESULT))
            writeInserts(ctx, out, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice3Results, RaceDataType.FREE_PRACTICE_3_RESULT))
            writeInserts(ctx, out, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceFreePractice4Results, RaceDataType.FREE_PRACTICE_4_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceQualifying1Results, RaceDataType.QUALIFYING_1_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceQualifying2Results, RaceDataType.QUALIFYING_2_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceQualifyingResults, RaceDataType.QUALIFYING_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceQualifyingResultMapper.unmap(db.splitted.raceSprintQualifyingResults, RaceDataType.SPRINT_QUALIFYING_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceStartingGridPositionMapper.unmap(db.splitted.raceSprintStartingGridPositions, RaceDataType.SPRINT_STARTING_GRID_POSITION))
            writeInserts(ctx, out, RACE_DATA, raceRaceResultMapper.unmap(db.splitted.raceSprintRaceResults, RaceDataType.SPRINT_RACE_RESULT))
            writeInserts(ctx, out, RACE_DATA, racePracticeResultMapper.unmap(db.splitted.raceWarmingUpResults, RaceDataType.WARMING_UP_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceStartingGridPositionMapper.unmap(db.splitted.raceStartingGridPositions, RaceDataType.STARTING_GRID_POSITION))
            writeInserts(ctx, out, RACE_DATA, raceRaceResultMapper.unmap(db.splitted.raceRaceResults, RaceDataType.RACE_RESULT))
            writeInserts(ctx, out, RACE_DATA, raceFastestLapMapper.unmap(db.splitted.raceFastestLaps, RaceDataType.FASTEST_LAP))
            writeInserts(ctx, out, RACE_DATA, racePitStopMapper.unmap(db.splitted.racePitStops, RaceDataType.PIT_STOP))
            writeInserts(ctx, out, RACE_DATA, raceDriverOfTheDayResultMapper.unmap(db.splitted.raceDriverOfTheDayResults, RaceDataType.DRIVER_OF_THE_DAY_RESULT))
            writeInserts(ctx, out, RACE_DRIVER_STANDING, raceDriverStandingMapper.unmap(db.splitted.raceDriverStandings))
            writeInserts(ctx, out, RACE_CONSTRUCTOR_STANDING, raceConstructorStandingMapper.unmap(db.splitted.raceConstructorStandings))
        }
    }

    private fun writeCreateSchema(ctx: DSLContext, out: PrintWriter) {
        ctx.settings().isRenderFormatted = true
        tables.forEach { table ->
            var stmt = ctx
                    .createTable(table)
                    .columns(*table.fields())
                    .constraint(primaryKey(table.primaryKey?.fields))
                    .constraints(table.uniqueKeys.map { unique(it.fields) })
                    .constraints(table.references.map { foreignKey(it.fields).references(it.inverseKey.table, it.keyFields) })
                    .sql
            out.println("$stmt;")
            out.println("")
            ctx.settings().isRenderFormatted = false
            table.indexes.forEach { index ->
                stmt = ctx
                        .createIndex(index.name)
                        .on(index.table, index.fields)
                        .sql
                out.println("$stmt;")
            }
            if (table.indexes.size > 0) {
                out.println("")
            }
            ctx.settings().isRenderFormatted = true
        }
    }

    private fun writeCreateViews(ctx: DSLContext, out: PrintWriter) {
        ctx.settings().isRenderFormatted = true
        views.forEach { view ->
            ctx.ddl(view).queries().forEach { query ->
                val stmt = query.sql
                out.println("$stmt;")
                out.println("")
            }
        }
    }

    private fun writeDropTables(ctx: DSLContext, out: PrintWriter) {
        ctx.settings().isRenderFormatted = false
        tables.reversed().forEach { table ->
            val stmt = ctx.dropTableIfExists(table).sql
            out.println("$stmt;")
        }
        out.println("")
    }

    private fun writeDropViews(ctx: DSLContext, out: PrintWriter) {
        ctx.settings().isRenderFormatted = false
        views.forEach { table ->
            val stmt = ctx.dropViewIfExists(table).sql
            out.println("$stmt;")
        }
        out.println("")
    }

    private fun <R : Record> writeInserts(ctx: DSLContext, out: PrintWriter, table: Table<R>, records: List<R>) {
        ctx.settings().isRenderFormatted = false
        records.forEach { record ->
            val stmt = ctx
                    .insertInto(table)
                    .set(record)
                    .sql
            out.println("$stmt;")
        }
        out.println("")
    }

    private val tables = listOf(
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

    private val views = listOf(
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
