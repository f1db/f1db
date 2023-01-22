package com.onlyf1.db.plugin.writer

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
import com.onlyf1.db.plugin.extensions.batchUpdate
import com.onlyf1.db.plugin.extensions.execute
import com.onlyf1.db.plugin.extensions.splitted
import com.onlyf1.db.schema.single.OnlyF1DB
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.sqlite.SQLiteDataSource
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

/**
 * The OnlyF1-DB writer.
 *
 * @author Marcel Overdijk
 */
class OnlyF1DBWriter(
    private val projectName: String,
    private val outputDir: File,
    private val schemaDir: File,
    private val db: OnlyF1DB,
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
        writeSplittedFile(format, "$projectName-races-sprint-qualifying-starting-grid-positions", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceSprintQualifyingStartingGridPositions)
        writeSplittedFile(format, "$projectName-races-sprint-qualifying-results", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceSprintQualifyingResults)
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

        val dataSource = SQLiteDataSource()
        dataSource.url = "jdbc:sqlite:${outputFile.absolutePath}"
        val jdbcTemplate = NamedParameterJdbcTemplate(dataSource)
        var sql: String
        var batchValues: List<Map<String, Any?>>

        // Create schema.

        sql = readResource("/sqlite/create_schema.sql")
        sql.split(";").filter { it.isNotBlank() }.forEach {
            jdbcTemplate.execute(it)
        }

        // Insert data.

        sql = readResource("/sqlite/insert_continent.sql")
        batchValues = db.splitted.continents.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_country.sql")
        batchValues = db.splitted.countries.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_driver.sql")
        batchValues = db.splitted.drivers.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_driver_family_relationship.sql")
        batchValues = db.splitted.driverFamilyRelationships.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_constructor.sql")
        batchValues = db.splitted.constructors.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_constructor_previous_next_constructor.sql")
        batchValues = db.splitted.constructorPreviousNextConstructors.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_engine_manufacturer.sql")
        batchValues = db.splitted.engineManufacturers.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_tyre_manufacturer.sql")
        batchValues = db.splitted.tyreManufacturers.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_entrant.sql")
        batchValues = db.splitted.entrants.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_circuit.sql")
        batchValues = db.splitted.circuits.map {
            val value = toMap(it).toMutableMap()
            value["previousNames"] = it.previousNames?.joinToString(";")
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_grand_prix.sql")
        batchValues = db.splitted.grandsPrix.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season.sql")
        batchValues = db.splitted.seasons.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season_entrant.sql")
        batchValues = db.splitted.seasonEntrants.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season_entrant_constructor.sql")
        batchValues = db.splitted.seasonEntrantConstructors.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season_entrant_tyre_manufacturer.sql")
        batchValues = db.splitted.seasonEntrantTyreManufacturers.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season_entrant_driver.sql")
        batchValues = db.splitted.seasonEntrantDrivers.map {
            val value = toMap(it).toMutableMap()
            value["rounds"] = it.rounds?.joinToString(";")
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season_driver_standing.sql")
        batchValues = db.splitted.seasonDriverStandings.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_season_constructor_standing.sql")
        batchValues = db.splitted.seasonConstructorStandings.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race.sql")
        batchValues = db.splitted.races.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_qualifying_result.sql")
        batchValues = db.splitted.racePreQualifyingResults.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "PRE_QUALIFYING_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_practice_result.sql")
        batchValues = db.splitted.raceFreePractice1Results.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "FREE_PRACTICE_1_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_practice_result.sql")
        batchValues = db.splitted.raceFreePractice2Results.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "FREE_PRACTICE_2_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_practice_result.sql")
        batchValues = db.splitted.raceFreePractice3Results.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "FREE_PRACTICE_3_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_practice_result.sql")
        batchValues = db.splitted.raceFreePractice4Results.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "FREE_PRACTICE_4_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_qualifying_result.sql")
        batchValues = db.splitted.raceQualifying1Results.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "QUALIFYING_1_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_qualifying_result.sql")
        batchValues = db.splitted.raceQualifying2Results.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "QUALIFYING_2_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_qualifying_result.sql")
        batchValues = db.splitted.raceQualifyingResults.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "QUALIFYING_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_practice_result.sql")
        batchValues = db.splitted.raceWarmingUpResults.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "WARMING_UP_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_starting_grid_position.sql")
        batchValues = db.splitted.raceSprintQualifyingStartingGridPositions.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "SPRINT_QUALIFYING_STARTING_GRID_POSITION"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_race_result.sql")
        batchValues = db.splitted.raceSprintQualifyingResults.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "SPRINT_QUALIFYING_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_starting_grid_position.sql")
        batchValues = db.splitted.raceStartingGridPositions.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "STARTING_GRID_POSITION"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_race_result.sql")
        batchValues = db.splitted.raceRaceResults.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "RACE_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_fastest_lap.sql")
        batchValues = db.splitted.raceFastestLaps.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "FASTEST_LAP"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_pit_stop.sql")
        batchValues = db.splitted.racePitStops.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "PIT_STOP"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_driver_of_the_day_result.sql")
        batchValues = db.splitted.raceDriverOfTheDayResults.map {
            val value = toMap(it).toMutableMap()
            value["type"] = "DRIVER_OF_THE_DAY_RESULT"
            value
        }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_driver_standing.sql")
        batchValues = db.splitted.raceDriverStandings.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        sql = readResource("/sqlite/insert_race_constructor_standing.sql")
        batchValues = db.splitted.raceConstructorStandings.map { toMap(it) }
        jdbcTemplate.batchUpdate(sql, batchValues)

        // Rebuild the database; repacking it into a minimal amount of disk space.

        jdbcTemplate.execute("VACUUM;")
    }

    private fun readResource(resource: String, charset: Charset = StandardCharsets.UTF_8): String {
        return javaClass.getResource(resource).readText(charset)
    }

    private fun <T : Any> toMap(obj: T): Map<String, Any?> {
        @Suppress("UNCHECKED_CAST")
        return (obj::class as KClass<T>).memberProperties.associate { prop ->
            prop.isAccessible = true
            prop.name to prop.get(obj)?.let { value ->
                if (value::class.isData) {
                    toMap(value)
                } else {
                    value
                }
            }
        }
    }
}
