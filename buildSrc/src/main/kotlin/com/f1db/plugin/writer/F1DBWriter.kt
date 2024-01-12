package com.f1db.plugin.writer

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.writer.sql.F1DBDao
import com.f1db.schema.single.F1db
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.smile.SmileFactory
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.sqlobject.kotlin.KotlinSqlObjectPlugin
import org.jdbi.v3.sqlobject.kotlin.onDemand
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.*

/**
 * The F1DB writer.
 *
 * @author Marcel Overdijk
 */
class F1DBWriter(
    private val projectName: String,
    private val outputDir: File,
    private val schemaDir: File,
    private val db: F1db,
) {

    private val csvOutputDir = File(outputDir, "csv")
    private val jsonOutputDir = File(outputDir, "json")
    private val smileOutputDir = File(outputDir, "smile")
    private val sqliteOutputDir = File(outputDir, "sqlite")

    enum class Format(val extension: String) {
        CSV("csv"),
        JSON("json"),
        SMILE("sml"),
        SQLITE("db")
    }

    fun write() {

        println("Writing data........")

        outputDir.deleteRecursively()
        outputDir.mkdirs()
        csvOutputDir.mkdirs()
        jsonOutputDir.mkdirs()
        smileOutputDir.mkdirs()
        sqliteOutputDir.mkdirs()

        write(Format.CSV)
        write(Format.JSON)
        write(Format.SMILE)
        write(Format.SQLITE)
    }

    private fun write(format: Format) {
        when (format) {

            Format.CSV -> {
                writeSplittedFiles(format)
            }

            Format.JSON,
            Format.SMILE -> {
                writeSingleFile(format)
                writeSplittedFiles(format)
            }

            Format.SQLITE -> {
                writeSingleFile(format)
            }
        }
    }

    private fun writeSingleFile(format: Format) {
        val outputFileName = "$projectName.${format.extension}"
        val schemaFileName = "single/$projectName.schema.json"
        val schemaFile = File(schemaDir, schemaFileName)
        when (format) {
            Format.JSON -> writeJsonFile(File(jsonOutputDir, outputFileName), schemaFile, db)
            Format.SMILE -> writeSmileFile(File(smileOutputDir, outputFileName), schemaFile, db)
            Format.SQLITE -> writeSQLiteFile(File(sqliteOutputDir, outputFileName))
            else -> throw IllegalArgumentException()
        }
    }

    private fun writeSplittedFiles(format: Format) {
        // @formatter:off
        writeSplittedFile(format, "$projectName-drivers", "splitted/$projectName-drivers.schema.json", db.splitted.drivers)
        writeSplittedFile(format, "$projectName-drivers-family-relationships", "splitted/$projectName-drivers-family-relationships.schema.json", db.splitted.driverFamilyRelationships)
        writeSplittedFile(format, "$projectName-constructors", "splitted/$projectName-constructors.schema.json", db.splitted.constructors)
        writeSplittedFile(format, "$projectName-constructors-previous-next-constructors", "splitted/$projectName-constructors-previous-next-constructors.schema.json", db.splitted.constructorPreviousNextConstructors)
        writeSplittedFile(format, "$projectName-engine-manufacturers", "splitted/$projectName-engine-manufacturers.schema.json", db.splitted.engineManufacturers)
        writeSplittedFile(format, "$projectName-tyre-manufacturers", "splitted/$projectName-tyre-manufacturers.schema.json", db.splitted.tyreManufacturers)
        writeSplittedFile(format, "$projectName-entrants", "splitted/$projectName-entrants.schema.json", db.splitted.entrants)
        writeSplittedFile(format, "$projectName-circuits", "splitted/$projectName-circuits.schema.json", db.splitted.circuits)
        writeSplittedFile(format, "$projectName-grands-prix", "splitted/$projectName-grands-prix.schema.json", db.splitted.grandsPrix)
        writeSplittedFile(format, "$projectName-seasons", "splitted/$projectName-seasons.schema.json", db.splitted.seasons)
        writeSplittedFile(format, "$projectName-seasons-entrants", "splitted/$projectName-seasons-entrants.schema.json", db.splitted.seasonEntrants)
        writeSplittedFile(format, "$projectName-seasons-entrants-constructors", "splitted/$projectName-seasons-entrants-constructors.schema.json", db.splitted.seasonEntrantConstructors)
        writeSplittedFile(format, "$projectName-seasons-entrants-tyre-manufacturers", "splitted/$projectName-seasons-entrants-tyre-manufacturers.schema.json", db.splitted.seasonEntrantTyreManufacturers)
        writeSplittedFile(format, "$projectName-seasons-entrants-drivers", "splitted/$projectName-seasons-entrants-drivers.schema.json", db.splitted.seasonEntrantDrivers)
        writeSplittedFile(format, "$projectName-seasons-driver-standings", "splitted/$projectName-seasons-driver-standings.schema.json", db.splitted.seasonDriverStandings)
        writeSplittedFile(format, "$projectName-seasons-constructor-standings", "splitted/$projectName-seasons-constructor-standings.schema.json", db.splitted.seasonConstructorStandings)
        writeSplittedFile(format, "$projectName-races", "splitted/$projectName-races.schema.json", db.splitted.races)
        writeSplittedFile(format, "$projectName-races-pre-qualifying-results", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.racePreQualifyingResults)
        writeSplittedFile(format, "$projectName-races-free-practice-1-results", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice1Results)
        writeSplittedFile(format, "$projectName-races-free-practice-2-results", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice2Results)
        writeSplittedFile(format, "$projectName-races-free-practice-3-results", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice3Results)
        writeSplittedFile(format, "$projectName-races-free-practice-4-results", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice4Results)
        writeSplittedFile(format, "$projectName-races-qualifying-1-results", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifying1Results)
        writeSplittedFile(format, "$projectName-races-qualifying-2-results", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifying2Results)
        writeSplittedFile(format, "$projectName-races-qualifying-results", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifyingResults)
        writeSplittedFile(format, "$projectName-races-sprint-qualifying-results", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceSprintQualifyingResults)
        writeSplittedFile(format, "$projectName-races-sprint-starting-grid-positions", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceSprintStartingGridPositions)
        writeSplittedFile(format, "$projectName-races-sprint-race-results", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceSprintRaceResults)
        writeSplittedFile(format, "$projectName-races-warming-up-results", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceWarmingUpResults)
        writeSplittedFile(format, "$projectName-races-starting-grid-positions", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceStartingGridPositions)
        writeSplittedFile(format, "$projectName-races-race-results", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceRaceResults)
        writeSplittedFile(format, "$projectName-races-fastest-laps", "splitted/$projectName-races-fastest-laps.schema.json", db.splitted.raceFastestLaps)
        writeSplittedFile(format, "$projectName-races-pit-stops", "splitted/$projectName-races-pit-stops.schema.json", db.splitted.racePitStops)
        writeSplittedFile(format, "$projectName-races-driver-of-the-day-results", "splitted/$projectName-races-driver-of-the-day-results.schema.json", db.splitted.raceDriverOfTheDayResults)
        writeSplittedFile(format, "$projectName-races-driver-standings", "splitted/$projectName-races-driver-standings.schema.json", db.splitted.raceDriverStandings)
        writeSplittedFile(format, "$projectName-races-constructor-standings", "splitted/$projectName-races-constructor-standings.schema.json", db.splitted.raceConstructorStandings)
        writeSplittedFile(format, "$projectName-continents", "splitted/$projectName-continents.schema.json", db.splitted.continents)
        writeSplittedFile(format, "$projectName-countries", "splitted/$projectName-countries.schema.json", db.splitted.countries)
        // @formatter:on
    }

    private fun writeSplittedFile(format: Format, outputFileName: String, schemaFileName: String, values: List<Any>) {
        val schemaFile = File(schemaDir, schemaFileName)
        when (format) {
            Format.CSV -> writeCsvFile(File(csvOutputDir, "$outputFileName.${format.extension}"), values)
            Format.JSON -> writeJsonFile(File(jsonOutputDir, "$outputFileName.${format.extension}"), schemaFile, values)
            Format.SMILE -> writeSmileFile(File(smileOutputDir, "$outputFileName.${format.extension}"), schemaFile, values)
            else -> throw IllegalArgumentException()
        }
    }

    private fun writeCsvFile(outputFile: File, values: List<Any>) {

        if (values.isEmpty()) {
            return
        }

        println("Writing ${outputFile.name}....")

        // Create the csv mapper.

        val mapper = CsvMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .addModule(JavaTimeModule())
            .addModule(KotlinModule.Builder().build())
            .build()

        // Write the csv file.

        val schemaClass = values[0].javaClass
        val schema = mapper.schemaFor(schemaClass).withHeader()
        mapper.writer(schema).writeValue(outputFile, values)
    }

    private fun writeJsonFile(outputFile: File, schemaFile: File, value: Any) {

        println("Writing ${outputFile.name}....")

        // Create the json mapper.

        val mapper = JsonMapper.builder()
            .defaultPrettyPrinter(DefaultPrettyPrinter().withArrayIndenter(DefaultPrettyPrinter.NopIndenter()).withoutSpacesInObjectEntries())
            .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .addModule(JavaTimeModule())
            .addModule(KotlinModule.Builder().build())
            .build()

        // Write the json file.

        mapper.writeValue(outputFile, value)

        // Validate the json file.

        val validationService = JsonValidationService.newInstance(ClassicJsonProvider())
        val reader = FileReader(outputFile)
        val schema = validationService.readSchema(FileReader(schemaFile))
        val handler = ProblemHandler.throwing()
        validationService.createParser(reader, schema, handler).use { parser ->
            while (parser.hasNext()) {
                parser.next()
            }
        }
    }

    private fun writeSmileFile(outputFile: File, schemaFile: File, value: Any) {

        println("Writing ${outputFile.name}....")

        // Create the smile mapper.

        val mapper = SmileMapper.builder()
            .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .addModule(JavaTimeModule())
            .addModule(KotlinModule.Builder().build())
            .build()

        // Write the smile file.

        mapper.writeValue(outputFile, value)

        // Validate the smile file.

        val json = ByteArrayOutputStream()
        SmileFactory().createParser(outputFile).use { parser ->
            // Convert smile to json.
            JsonFactory().createGenerator(json).use { generator ->
                while (parser.nextToken() != null) {
                    generator.copyCurrentEvent(parser)
                }
            }
        }
        val validationService = JsonValidationService.newInstance(ClassicJsonProvider())
        val reader = InputStreamReader(ByteArrayInputStream(json.toByteArray()))
        val schema = validationService.readSchema(FileReader(schemaFile))
        val handler = ProblemHandler.throwing()
        validationService.createParser(reader, schema, handler).use { parser ->
            while (parser.hasNext()) {
                parser.next()
            }
        }
    }

    private fun writeSQLiteFile(outputFile: File) {

        println("Writing ${outputFile.name}....")

        Class.forName("org.sqlite.JDBC")

        val jdbi = Jdbi.create("jdbc:sqlite:${outputFile.absolutePath}", Properties().apply {
            setProperty("date_class", "text")
            setProperty("date_string_format", "yyyy-MM-dd")
        })
        jdbi.installPlugin(KotlinPlugin())
        jdbi.installPlugin(KotlinSqlObjectPlugin())

        val dao = jdbi.onDemand<F1DBDao>()

        // Create schema.

        dao.createSchema()

        // Insert data.

        dao.insertContinents(db.splitted.continents)
        dao.insertCountries(db.splitted.countries)
        dao.insertDrivers(db.splitted.drivers)
        dao.insertDriverFamilyRelationships(db.splitted.driverFamilyRelationships)
        dao.insertConstructors(db.splitted.constructors)
        dao.insertConstructorPreviousNextConstructors(db.splitted.constructorPreviousNextConstructors)
        dao.insertEngineManufacturers(db.splitted.engineManufacturers)
        dao.insertTyreManufacturers(db.splitted.tyreManufacturers)
        dao.insertEntrants(db.splitted.entrants)
        dao.insertCircuits(db.splitted.circuits, db.splitted.circuits.map { it.previousNames?.joinToString(";") })
        dao.insertGrandsPrix(db.splitted.grandsPrix)
        dao.insertSeasons(db.splitted.seasons)
        dao.insertSeasonEntrants(db.splitted.seasonEntrants)
        dao.insertSeasonEntrantConstructors(db.splitted.seasonEntrantConstructors)
        dao.insertSeasonEntrantTyreManufacturers(db.splitted.seasonEntrantTyreManufacturers)
        dao.insertSeasonEntrantDrivers(db.splitted.seasonEntrantDrivers, db.splitted.seasonEntrantDrivers.map { it.rounds?.joinToString(";") })
        dao.insertSeasonDriverStandings(db.splitted.seasonDriverStandings)
        dao.insertSeasonConstructorStandings(db.splitted.seasonConstructorStandings)
        dao.insertRaces(db.splitted.races)
        dao.insertRaceQualifyingResult(db.splitted.racePreQualifyingResults, "PRE_QUALIFYING_RESULT")
        dao.insertRacePracticeResult(db.splitted.raceFreePractice1Results, "FREE_PRACTICE_1_RESULT")
        dao.insertRacePracticeResult(db.splitted.raceFreePractice2Results, "FREE_PRACTICE_2_RESULT")
        dao.insertRacePracticeResult(db.splitted.raceFreePractice3Results, "FREE_PRACTICE_3_RESULT")
        dao.insertRacePracticeResult(db.splitted.raceFreePractice4Results, "FREE_PRACTICE_4_RESULT")
        dao.insertRaceQualifyingResult(db.splitted.raceQualifying1Results, "QUALIFYING_1_RESULT")
        dao.insertRaceQualifyingResult(db.splitted.raceQualifying2Results, "QUALIFYING_2_RESULT")
        dao.insertRaceQualifyingResult(db.splitted.raceQualifyingResults, "QUALIFYING_RESULT")
        dao.insertRaceQualifyingResult(db.splitted.raceSprintQualifyingResults, "SPRINT_QUALIFYING_RESULT")
        dao.insertRaceStartingGridPosition(db.splitted.raceSprintStartingGridPositions, "SPRINT_STARTING_GRID_POSITION")
        dao.insertRaceRaceResult(db.splitted.raceSprintRaceResults, "SPRINT_RACE_RESULT")
        dao.insertRacePracticeResult(db.splitted.raceWarmingUpResults, "WARMING_UP_RESULT")
        dao.insertRaceStartingGridPosition(db.splitted.raceStartingGridPositions, "STARTING_GRID_POSITION")
        dao.insertRaceRaceResult(db.splitted.raceRaceResults, "RACE_RESULT")
        dao.insertRaceFastestLap(db.splitted.raceFastestLaps, "FASTEST_LAP")
        dao.insertRacePitStop(db.splitted.racePitStops, "PIT_STOP")
        dao.insertRaceDriverOfTheDayResult(db.splitted.raceDriverOfTheDayResults, "DRIVER_OF_THE_DAY_RESULT")
        dao.insertRaceDriverStandings(db.splitted.raceDriverStandings)
        dao.insertRaceConstructorStandings(db.splitted.raceConstructorStandings)

        // Rebuild the database; repacking it into a minimal amount of disk space.

        dao.vacuum()
    }
}
