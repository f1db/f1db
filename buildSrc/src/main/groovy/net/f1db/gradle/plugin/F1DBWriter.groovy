package net.f1db.gradle.plugin

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.smile.SmileFactory
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import groovy.sql.Sql
import net.f1db.Constructor
import net.f1db.Driver
import net.f1db.DriverOfTheDayResult
import net.f1db.F1db
import net.f1db.FastestLap
import net.f1db.PitStop
import net.f1db.PracticeResult
import net.f1db.QualifyingResult
import net.f1db.Race
import net.f1db.RaceConstructorStanding
import net.f1db.RaceDriverStanding
import net.f1db.RaceResult
import net.f1db.Season
import net.f1db.SeasonConstructorStanding
import net.f1db.SeasonDriverStanding
import net.f1db.StartingGridPosition
import net.f1db.gradle.plugin.databind.splitted.SplittedConstructorMixIn
import net.f1db.gradle.plugin.databind.splitted.SplittedDriverMixIn
import net.f1db.gradle.plugin.databind.splitted.SplittedRaceMixIn
import net.f1db.gradle.plugin.databind.splitted.SplittedSeasonEntrantConstructorMixIn
import net.f1db.gradle.plugin.databind.splitted.SplittedSeasonEntrantMixIn
import net.f1db.gradle.plugin.databind.splitted.SplittedSeasonMixIn
import net.f1db.gradle.plugin.dto.splitted.SplittedDriverFamilyRelationship
import net.f1db.gradle.plugin.dto.splitted.SplittedDriverOfTheDayResult
import net.f1db.gradle.plugin.dto.splitted.SplittedFastestLap
import net.f1db.gradle.plugin.dto.splitted.SplittedPitStop
import net.f1db.gradle.plugin.dto.splitted.SplittedPracticeResult
import net.f1db.gradle.plugin.dto.splitted.SplittedPreviousNextConstructor
import net.f1db.gradle.plugin.dto.splitted.SplittedQualifyingResult
import net.f1db.gradle.plugin.dto.splitted.SplittedRaceConstructorStanding
import net.f1db.gradle.plugin.dto.splitted.SplittedRaceDriverStanding
import net.f1db.gradle.plugin.dto.splitted.SplittedRaceResult
import net.f1db.gradle.plugin.dto.splitted.SplittedSeasonConstructorStanding
import net.f1db.gradle.plugin.dto.splitted.SplittedSeasonDriverStanding
import net.f1db.gradle.plugin.dto.splitted.SplittedSeasonEntrant
import net.f1db.gradle.plugin.dto.splitted.SplittedSeasonEntrantConstructor
import net.f1db.gradle.plugin.dto.splitted.SplittedSeasonEntrantDriver
import net.f1db.gradle.plugin.dto.splitted.SplittedSeasonEntrantTyreManufacturer
import net.f1db.gradle.plugin.dto.splitted.SplittedStartingGridPosition
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler

import java.nio.charset.StandardCharsets

import static F1DBFileName.CIRCUITS_FILE_NAME
import static F1DBFileName.CONSTRUCTORS_FILE_NAME
import static F1DBFileName.CONSTRUCTORS_PREVIOUS_NEXT_CONSTRUCTORS_FILE_NAME
import static F1DBFileName.CONTINENTS_FILE_NAME
import static F1DBFileName.COUNTRIES_FILE_NAME
import static F1DBFileName.DRIVERS_FAMILY_RELATIONSHIPS_FILE_NAME
import static F1DBFileName.DRIVERS_FILE_NAME
import static F1DBFileName.ENGINE_MANUFACTURERS_FILE_NAME
import static F1DBFileName.ENTRANTS_FILE_NAME
import static F1DBFileName.F1DB_FILE_NAME
import static F1DBFileName.GRANDS_PRIX_FILE_NAME
import static F1DBFileName.RACES_CONSTRUCTOR_STANDINGS_FILE_NAME
import static F1DBFileName.RACES_DRIVER_OF_THE_DAY_RESULTS_FILE_NAME
import static F1DBFileName.RACES_DRIVER_STANDINGS_FILE_NAME
import static F1DBFileName.RACES_FASTEST_LAPS_FILE_NAME
import static F1DBFileName.RACES_FILE_NAME
import static F1DBFileName.RACES_FREE_PRACTICE_1_RESULTS_FILE_NAME
import static F1DBFileName.RACES_FREE_PRACTICE_2_RESULTS_FILE_NAME
import static F1DBFileName.RACES_FREE_PRACTICE_3_RESULTS_FILE_NAME
import static F1DBFileName.RACES_FREE_PRACTICE_4_RESULTS_FILE_NAME
import static F1DBFileName.RACES_PIT_STOPS_FILE_NAME
import static F1DBFileName.RACES_PRE_QUALIFYING_RESULTS_FILE_NAME
import static F1DBFileName.RACES_QUALIFYING_1_RESULTS_FILE_NAME
import static F1DBFileName.RACES_QUALIFYING_2_RESULTS_FILE_NAME
import static F1DBFileName.RACES_QUALIFYING_RESULTS_FILE_NAME
import static F1DBFileName.RACES_RACE_RESULTS_FILE_NAME
import static F1DBFileName.RACES_SPRINT_QUALIFYING_RESULTS_FILE_NAME
import static F1DBFileName.RACES_SPRINT_QUALIFYING_STARTING_GRID_POSITIONS_FILE_NAME
import static F1DBFileName.RACES_STARTING_GRID_POSITIONS_FILE_NAME
import static F1DBFileName.RACES_WARMING_UP_RESULTS_FILE_NAME
import static F1DBFileName.SEASONS_CONSTRUCTOR_STANDINGS_FILE_NAME
import static F1DBFileName.SEASONS_DRIVER_STANDINGS_FILE_NAME
import static F1DBFileName.SEASONS_ENTRANTS_CONSTRUCTORS_FILE_NAME
import static F1DBFileName.SEASONS_ENTRANTS_DRIVERS_FILE_NAME
import static F1DBFileName.SEASONS_ENTRANTS_FILE_NAME
import static F1DBFileName.SEASONS_ENTRANTS_TYRE_MANUFACTURERS_FILE_NAME
import static F1DBFileName.SEASONS_FILE_NAME
import static F1DBFileName.TYRE_MANUFACTURERS_FILE_NAME

/**
 * The F1DB data writer.
 *
 * @author Marcel Overdijk
 */
class F1DBWriter {

    File JsonSchemaFile
    File jsonSchemaFileSplitted
    File outputDir
    File csvOutputDir
    File jsonOutputDir
    File jsonSplittedOutputDir
    File smileOutputDir
    File smileSplittedOutputDir
    File sqliteOutputDir
    boolean indentOutput

    F1db f1db

    F1DBWriter(File projectDir, F1DBPluginExtension extension, F1db f1db) {

        this.jsonSchemaFile = new File(projectDir, "src/schema/f1db-json-schema-v${extension.jsonSchemaVersion}.json")
        this.jsonSchemaFileSplitted = new File(projectDir, "src/schema/f1db-json-schema-v${extension.jsonSchemaVersion}-splitted.json")
        this.outputDir = new File(projectDir, "build/data")
        this.csvOutputDir = new File(outputDir, "csv")
        this.jsonOutputDir = new File(outputDir, "json")
        this.jsonSplittedOutputDir = new File(outputDir, "json-splitted")
        this.smileOutputDir = new File(outputDir, "smile")
        this.smileSplittedOutputDir = new File(outputDir, "smile-splitted")
        this.sqliteOutputDir = new File(outputDir, "sqlite")
        this.indentOutput = extension.indentOutput
        this.f1db = f1db
    }

    void write() {

        println "Writing data........."

        outputDir.deleteDir()
        outputDir.mkdirs()
        csvOutputDir.mkdirs()
        jsonOutputDir.mkdirs()
        jsonSplittedOutputDir.mkdirs()
        smileOutputDir.mkdirs()
        smileSplittedOutputDir.mkdirs()
        sqliteOutputDir.mkdirs()

        writeCsv()
        writeJson()
        writeSmile()
        writeSQLiteDatabase()
    }

    private void writeCsv() {

        // Write the splitted csv files.

        writeCsv(CONTINENTS_FILE_NAME, f1db.continents)
        writeCsv(COUNTRIES_FILE_NAME, f1db.countries)
        writeCsv(DRIVERS_FILE_NAME, f1db.drivers)
        writeCsv(DRIVERS_FAMILY_RELATIONSHIPS_FILE_NAME, getSplittedDriversFamilyRelationships())
        writeCsv(CONSTRUCTORS_FILE_NAME, f1db.constructors)
        writeCsv(CONSTRUCTORS_PREVIOUS_NEXT_CONSTRUCTORS_FILE_NAME, getSplittedConstructorsPreviousNextConstructors())
        writeCsv(ENGINE_MANUFACTURERS_FILE_NAME, f1db.engineManufacturers)
        writeCsv(TYRE_MANUFACTURERS_FILE_NAME, f1db.tyreManufacturers)
        writeCsv(ENTRANTS_FILE_NAME, f1db.entrants)
        writeCsv(CIRCUITS_FILE_NAME, f1db.circuits)
        writeCsv(GRANDS_PRIX_FILE_NAME, f1db.grandsPrix)
        writeCsv(SEASONS_FILE_NAME, f1db.seasons)
        writeCsv(SEASONS_ENTRANTS_FILE_NAME, getSplittedSeasonsEntrants())
        writeCsv(SEASONS_ENTRANTS_CONSTRUCTORS_FILE_NAME, getSplittedSeasonsEntrantsConstructors())
        writeCsv(SEASONS_ENTRANTS_TYRE_MANUFACTURERS_FILE_NAME, getSplittedSeasonsEntrantsTyreManufacturers())
        writeCsv(SEASONS_ENTRANTS_DRIVERS_FILE_NAME, getSplittedSeasonsEntrantsDrivers())
        writeCsv(SEASONS_DRIVER_STANDINGS_FILE_NAME, getSplittedSeasonsDriverStandings())
        writeCsv(SEASONS_CONSTRUCTOR_STANDINGS_FILE_NAME, getSplittedSeasonsConstructorStandings())
        writeCsv(RACES_FILE_NAME, f1db.races)
        writeCsv(RACES_PRE_QUALIFYING_RESULTS_FILE_NAME, getSplittedRacesPreQualifyingResults())
        writeCsv(RACES_FREE_PRACTICE_1_RESULTS_FILE_NAME, getSplittedRacesFreePractice1Results())
        writeCsv(RACES_FREE_PRACTICE_2_RESULTS_FILE_NAME, getSplittedRacesFreePractice2Results())
        writeCsv(RACES_FREE_PRACTICE_3_RESULTS_FILE_NAME, getSplittedRacesFreePractice3Results())
        writeCsv(RACES_FREE_PRACTICE_4_RESULTS_FILE_NAME, getSplittedRacesFreePractice4Results())
        writeCsv(RACES_QUALIFYING_1_RESULTS_FILE_NAME, getSplittedRacesQualifying1Results())
        writeCsv(RACES_QUALIFYING_2_RESULTS_FILE_NAME, getSplittedRacesQualifying2Results())
        writeCsv(RACES_QUALIFYING_RESULTS_FILE_NAME, getSplittedRacesQualifyingResults())
        writeCsv(RACES_SPRINT_QUALIFYING_STARTING_GRID_POSITIONS_FILE_NAME, getSplittedRacesSprintQualifyingStartingGridPositions())
        writeCsv(RACES_SPRINT_QUALIFYING_RESULTS_FILE_NAME, getSplittedRacesSprintQualifyingResults())
        writeCsv(RACES_WARMING_UP_RESULTS_FILE_NAME, getSplittedRacesWarmingUpResults())
        writeCsv(RACES_STARTING_GRID_POSITIONS_FILE_NAME, getSplittedRacesStartingGridPositions())
        writeCsv(RACES_RACE_RESULTS_FILE_NAME, getSplittedRacesRaceResults())
        writeCsv(RACES_FASTEST_LAPS_FILE_NAME, getSplittedRacesFastestLaps())
        writeCsv(RACES_PIT_STOPS_FILE_NAME, getSplittedRacesPitStops())
        writeCsv(RACES_DRIVER_OF_THE_DAY_RESULTS_FILE_NAME, getSplittedRacesDriverOfTheDayResults())
        writeCsv(RACES_DRIVER_STANDINGS_FILE_NAME, getSplittedRacesDriverStandings())
        writeCsv(RACES_CONSTRUCTOR_STANDINGS_FILE_NAME, getSplittedRacesConstructorStandings())
    }

    private void writeCsv(F1DBFileName fileName, List<Object> values) {

        def outputFile = new File(csvOutputDir, fileName.csv())

        println "Writing ${outputFile.name}...."

        // Create the csv mapper.

        def mapper = createCsvMapper()

        // Write the csv file.

        def schemaClass = values[0].class
        def schema = mapper.schemaFor(schemaClass).withHeader()
        mapper.writer(schema).writeValue(outputFile, values)
    }

    private void writeJson() {

        def outputFile = new File(jsonOutputDir, F1DB_FILE_NAME.json())

        println "Writing ${outputFile.name}...."

        // Create the json mapper.

        def mapper = createJsonMapper()

        // Write the single json file.

        mapper.writeValue(outputFile, f1db)

        // Validate the single json file.

        validateJson(jsonSchemaFile, outputFile)

        // Write the splitted json files.

        writeJson(CONTINENTS_FILE_NAME, [continents: f1db.continents])
        writeJson(COUNTRIES_FILE_NAME, [countries: f1db.countries])
        writeJson(DRIVERS_FILE_NAME, [drivers: f1db.drivers])
        writeJson(DRIVERS_FAMILY_RELATIONSHIPS_FILE_NAME, [driversFamilyRelationships: getSplittedDriversFamilyRelationships()])
        writeJson(CONSTRUCTORS_FILE_NAME, [constructors: f1db.constructors])
        writeJson(CONSTRUCTORS_PREVIOUS_NEXT_CONSTRUCTORS_FILE_NAME, [constructorsPreviousNextConstructors: getSplittedConstructorsPreviousNextConstructors()])
        writeJson(ENGINE_MANUFACTURERS_FILE_NAME, [engineManufacturers: f1db.engineManufacturers])
        writeJson(TYRE_MANUFACTURERS_FILE_NAME, [tyreManufacturers: f1db.tyreManufacturers])
        writeJson(ENTRANTS_FILE_NAME, [entrants: f1db.entrants])
        writeJson(CIRCUITS_FILE_NAME, [circuits: f1db.circuits])
        writeJson(GRANDS_PRIX_FILE_NAME, [grandsPrix: f1db.grandsPrix])
        writeJson(SEASONS_FILE_NAME, [seasons: f1db.seasons])
        writeJson(SEASONS_ENTRANTS_FILE_NAME, [seasonsEntrants: getSplittedSeasonsEntrants()])
        writeJson(SEASONS_ENTRANTS_CONSTRUCTORS_FILE_NAME, [seasonsEntrantsConstructors: getSplittedSeasonsEntrantsConstructors()])
        writeJson(SEASONS_ENTRANTS_TYRE_MANUFACTURERS_FILE_NAME, [seasonsEntrantsTyreManufacturers: getSplittedSeasonsEntrantsTyreManufacturers()])
        writeJson(SEASONS_ENTRANTS_DRIVERS_FILE_NAME, [seasonsEntrantsDrivers: getSplittedSeasonsEntrantsDrivers()])
        writeJson(SEASONS_DRIVER_STANDINGS_FILE_NAME, [seasonsDriverStandings: getSplittedSeasonsDriverStandings()])
        writeJson(SEASONS_CONSTRUCTOR_STANDINGS_FILE_NAME, [seasonsConstructorStandings: getSplittedSeasonsConstructorStandings()])
        writeJson(RACES_FILE_NAME, [races: f1db.races])
        writeJson(RACES_PRE_QUALIFYING_RESULTS_FILE_NAME, [racesPreQualifyingResults: getSplittedRacesPreQualifyingResults()])
        writeJson(RACES_FREE_PRACTICE_1_RESULTS_FILE_NAME, [racesFreePractice1Results: getSplittedRacesFreePractice1Results()])
        writeJson(RACES_FREE_PRACTICE_2_RESULTS_FILE_NAME, [racesFreePractice2Results: getSplittedRacesFreePractice2Results()])
        writeJson(RACES_FREE_PRACTICE_3_RESULTS_FILE_NAME, [racesFreePractice3Results: getSplittedRacesFreePractice3Results()])
        writeJson(RACES_FREE_PRACTICE_4_RESULTS_FILE_NAME, [racesFreePractice4Results: getSplittedRacesFreePractice4Results()])
        writeJson(RACES_QUALIFYING_1_RESULTS_FILE_NAME, [racesQualifying1Results: getSplittedRacesQualifying1Results()])
        writeJson(RACES_QUALIFYING_2_RESULTS_FILE_NAME, [racesQualifying2Results: getSplittedRacesQualifying2Results()])
        writeJson(RACES_QUALIFYING_RESULTS_FILE_NAME, [racesQualifyingResults: getSplittedRacesQualifyingResults()])
        writeJson(RACES_SPRINT_QUALIFYING_STARTING_GRID_POSITIONS_FILE_NAME, [racesSprintQualifyingStartingGridPositions: getSplittedRacesSprintQualifyingStartingGridPositions()])
        writeJson(RACES_SPRINT_QUALIFYING_RESULTS_FILE_NAME, [racesSprintQualifyingResults: getSplittedRacesSprintQualifyingResults()])
        writeJson(RACES_WARMING_UP_RESULTS_FILE_NAME, [racesWarmingUpResults: getSplittedRacesWarmingUpResults()])
        writeJson(RACES_STARTING_GRID_POSITIONS_FILE_NAME, [racesStartingGridPositions: getSplittedRacesStartingGridPositions()])
        writeJson(RACES_RACE_RESULTS_FILE_NAME, [racesRaceResults: getSplittedRacesRaceResults()])
        writeJson(RACES_FASTEST_LAPS_FILE_NAME, [racesFastestLaps: getSplittedRacesFastestLaps()])
        writeJson(RACES_PIT_STOPS_FILE_NAME, [racesPitStops: getSplittedRacesPitStops()])
        writeJson(RACES_DRIVER_OF_THE_DAY_RESULTS_FILE_NAME, [racesDriverOfTheDayResults: getSplittedRacesDriverOfTheDayResults()])
        writeJson(RACES_DRIVER_STANDINGS_FILE_NAME, [racesDriverStandings: getSplittedRacesDriverStandings()])
        writeJson(RACES_CONSTRUCTOR_STANDINGS_FILE_NAME, [racesConstructorStandings: getSplittedRacesConstructorStandings()])
    }

    private void writeJson(F1DBFileName fileName, Object value) {

        def outputFile = new File(jsonSplittedOutputDir, fileName.json())

        println "Writing ${outputFile.name}...."

        // Create the json mapper.

        def mapper = createJsonMapper()
        mapper.registerModule(createSplittedModule())

        // Write the json file.

        mapper.writeValue(outputFile, value)

        // Validate the json file.

        validateJson(jsonSchemaFileSplitted, outputFile)
    }

    private void validateJson(File jsonSchemaFile, File outputFile) {
        def validationService = JsonValidationService.newInstance(new ClassicJsonProvider())
        def schema = validationService.readSchema(new FileReader(jsonSchemaFile))
        def reader = new FileReader(outputFile)
        def handler = ProblemHandler.throwing()
        validationService.createParser(reader, schema, handler).withCloseable { parser ->
            while (parser.hasNext()) {
                parser.next()
            }
        }
    }

    private void writeSmile() {

        def outputFile = new File(smileOutputDir, F1DB_FILE_NAME.smile())

        println "Writing ${outputFile.name}...."

        // Create the smile mapper.

        def mapper = createSmileMapper()

        // Write the single smile file.

        mapper.writeValue(outputFile, f1db)

        // Validate the single smile file.

        validateSmile(jsonSchemaFile, outputFile)

        // Write the splitted smile files.

        writeSmile(CONTINENTS_FILE_NAME, [continents: f1db.continents])
        writeSmile(COUNTRIES_FILE_NAME, [countries: f1db.countries])
        writeSmile(DRIVERS_FILE_NAME, [drivers: f1db.drivers])
        writeSmile(DRIVERS_FAMILY_RELATIONSHIPS_FILE_NAME, [driversFamilyRelationships: getSplittedDriversFamilyRelationships()])
        writeSmile(CONSTRUCTORS_FILE_NAME, [constructors: f1db.constructors])
        writeSmile(CONSTRUCTORS_PREVIOUS_NEXT_CONSTRUCTORS_FILE_NAME, [constructorsPreviousNextConstructors: getSplittedConstructorsPreviousNextConstructors()])
        writeSmile(ENGINE_MANUFACTURERS_FILE_NAME, [engineManufacturers: f1db.engineManufacturers])
        writeSmile(TYRE_MANUFACTURERS_FILE_NAME, [tyreManufacturers: f1db.tyreManufacturers])
        writeSmile(ENTRANTS_FILE_NAME, [entrants: f1db.entrants])
        writeSmile(CIRCUITS_FILE_NAME, [circuits: f1db.circuits])
        writeSmile(GRANDS_PRIX_FILE_NAME, [grandsPrix: f1db.grandsPrix])
        writeSmile(SEASONS_FILE_NAME, [seasons: f1db.seasons])
        writeSmile(SEASONS_ENTRANTS_FILE_NAME, [seasonsEntrants: getSplittedSeasonsEntrants()])
        writeSmile(SEASONS_ENTRANTS_CONSTRUCTORS_FILE_NAME, [seasonsEntrantsConstructors: getSplittedSeasonsEntrantsConstructors()])
        writeSmile(SEASONS_ENTRANTS_TYRE_MANUFACTURERS_FILE_NAME, [seasonsEntrantsTyreManufacturers: getSplittedSeasonsEntrantsTyreManufacturers()])
        writeSmile(SEASONS_ENTRANTS_DRIVERS_FILE_NAME, [seasonsEntrantsDrivers: getSplittedSeasonsEntrantsDrivers()])
        writeSmile(SEASONS_DRIVER_STANDINGS_FILE_NAME, [seasonsDriverStandings: getSplittedSeasonsDriverStandings()])
        writeSmile(SEASONS_CONSTRUCTOR_STANDINGS_FILE_NAME, [seasonsConstructorStandings: getSplittedSeasonsConstructorStandings()])
        writeSmile(RACES_FILE_NAME, [races: f1db.races])
        writeSmile(RACES_PRE_QUALIFYING_RESULTS_FILE_NAME, [racesPreQualifyingResults: getSplittedRacesPreQualifyingResults()])
        writeSmile(RACES_FREE_PRACTICE_1_RESULTS_FILE_NAME, [racesFreePractice1Results: getSplittedRacesFreePractice1Results()])
        writeSmile(RACES_FREE_PRACTICE_2_RESULTS_FILE_NAME, [racesFreePractice2Results: getSplittedRacesFreePractice2Results()])
        writeSmile(RACES_FREE_PRACTICE_3_RESULTS_FILE_NAME, [racesFreePractice3Results: getSplittedRacesFreePractice3Results()])
        writeSmile(RACES_FREE_PRACTICE_4_RESULTS_FILE_NAME, [racesFreePractice4Results: getSplittedRacesFreePractice4Results()])
        writeSmile(RACES_QUALIFYING_1_RESULTS_FILE_NAME, [racesQualifying1Results: getSplittedRacesQualifying1Results()])
        writeSmile(RACES_QUALIFYING_2_RESULTS_FILE_NAME, [racesQualifying2Results: getSplittedRacesQualifying2Results()])
        writeSmile(RACES_QUALIFYING_RESULTS_FILE_NAME, [racesQualifyingResults: getSplittedRacesQualifyingResults()])
        writeSmile(RACES_SPRINT_QUALIFYING_STARTING_GRID_POSITIONS_FILE_NAME, [racesSprintQualifyingStartingGridPositions: getSplittedRacesSprintQualifyingStartingGridPositions()])
        writeSmile(RACES_SPRINT_QUALIFYING_RESULTS_FILE_NAME, [racesSprintQualifyingResults: getSplittedRacesSprintQualifyingResults()])
        writeSmile(RACES_WARMING_UP_RESULTS_FILE_NAME, [racesWarmingUpResults: getSplittedRacesWarmingUpResults()])
        writeSmile(RACES_STARTING_GRID_POSITIONS_FILE_NAME, [racesStartingGridPositions: getSplittedRacesStartingGridPositions()])
        writeSmile(RACES_RACE_RESULTS_FILE_NAME, [racesRaceResults: getSplittedRacesRaceResults()])
        writeSmile(RACES_FASTEST_LAPS_FILE_NAME, [racesFastestLaps: getSplittedRacesFastestLaps()])
        writeSmile(RACES_PIT_STOPS_FILE_NAME, [racesPitStops: getSplittedRacesPitStops()])
        writeSmile(RACES_DRIVER_OF_THE_DAY_RESULTS_FILE_NAME, [racesDriverOfTheDayResults: getSplittedRacesDriverOfTheDayResults()])
        writeSmile(RACES_DRIVER_STANDINGS_FILE_NAME, [racesDriverStandings: getSplittedRacesDriverStandings()])
        writeSmile(RACES_CONSTRUCTOR_STANDINGS_FILE_NAME, [racesConstructorStandings: getSplittedRacesConstructorStandings()])
    }

    private void writeSmile(F1DBFileName fileName, Object value) {

        def outputFile = new File(smileSplittedOutputDir, fileName.smile())

        println "Writing ${outputFile.name}...."

        // Create the smile mapper.

        def mapper = createSmileMapper()
        mapper.registerModule(createSplittedModule())

        // Write the smile file.

        mapper.writeValue(outputFile, value)

        // Validate the smile file.

        validateSmile(jsonSchemaFileSplitted, outputFile)
    }

    private void validateSmile(File jsonSchemaFile, File outputFile) {
        def validationService = JsonValidationService.newInstance(new ClassicJsonProvider())
        def schema = validationService.readSchema(new FileReader(jsonSchemaFile))
        def jsonOutputStream = new ByteArrayOutputStream()
        new SmileFactory().createParser(outputFile).withCloseable { parser ->
            // Convert smile to json.
            new JsonFactory().createGenerator(jsonOutputStream).withCloseable { generator ->
                while (parser.nextToken()) {
                    generator.copyCurrentEvent(parser)
                }
            }
        }
        def reader = new InputStreamReader(new ByteArrayInputStream(jsonOutputStream.toByteArray()))
        def handler = ProblemHandler.throwing()
        validationService.createParser(reader, schema, handler).withCloseable { parser ->
            while (parser.hasNext()) {
                parser.next()
            }
        }
    }

    private void writeSQLiteDatabase() {

        def outputFile = new File(sqliteOutputDir, "f1db.db")

        println "Writing ${outputFile.name}......"

        def batchSize = 100
        def sql
        def url = "jdbc:sqlite:${outputFile.absolutePath}"
        def driverClassName = "org.sqlite.JDBC"

        Sql.withInstance(url, driverClassName) { sqlite ->

            // Create schema.

            def statements = readResource("/sqlite/create_schema.sql").split(";")
            statements.each { statement ->
                statement = statement.trim()
                if (statement) {
                    sqlite.execute(statement)
                }
            }

            // Insert continents.

            sql = readResource("/sqlite/insert_continent.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.continents.each { continent ->
                    ps.addBatch(continent)
                }
            }

            // Insert countries.

            sql = readResource("/sqlite/insert_country.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.countries.each { country ->
                    ps.addBatch(country)
                }
            }

            // Insert drivers.

            sql = readResource("/sqlite/insert_driver.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.drivers.each { driver ->
                    ps.addBatch(driver)
                }
            }

            sql = readResource("/sqlite/insert_driver_family_relationship.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.drivers.each { driver ->
                    driver.familyRelationships.each { familyRelationships ->
                        ps.addBatch(driver, familyRelationships)
                    }
                }
            }

            // Insert constructors.

            sql = readResource("/sqlite/insert_constructor.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.constructors.each { constructor ->
                    ps.addBatch(constructor)
                }
            }

            sql = readResource("/sqlite/insert_constructor_previous_next_constructor.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.constructors.each { constructor ->
                    constructor.previousNextConstructors.each { previousNextConstructor ->
                        ps.addBatch(constructor, previousNextConstructor)
                    }
                }
            }

            // Insert engine manufacturers.

            sql = readResource("/sqlite/insert_engine_manufacturer.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.engineManufacturers.each { engineManufacturer ->
                    ps.addBatch(engineManufacturer)
                }
            }

            // Insert tyre manufacturers.

            sql = readResource("/sqlite/insert_tyre_manufacturer.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.tyreManufacturers.each { tyreManufacturer ->
                    ps.addBatch(tyreManufacturer)
                }
            }

            // Insert entrant.

            sql = readResource("/sqlite/insert_entrant.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.entrants.each { entrant ->
                    ps.addBatch(entrant)
                }
            }

            // Insert circuit.

            sql = readResource("/sqlite/insert_circuit.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.circuits.each { circuit ->
                    ps.addBatch(circuit, [previousNames: circuit.previousNames?.join(",")])
                }
            }

            // Insert grand prix.

            sql = readResource("/sqlite/insert_grand_prix.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.grandsPrix.each { grandPrix ->
                    ps.addBatch(grandPrix)
                }
            }

            // Insert seasons.

            sql = readResource("/sqlite/insert_season.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.seasons.each { season ->
                    ps.addBatch(season)
                }
            }

            f1db.seasons.each { season ->

                // Insert entrants.

                sql = readResource("/sqlite/insert_season_entrant.sql")
                sqlite.withBatch(batchSize, sql) { ps ->
                    season.entrants.each { entrant ->
                        ps.addBatch(season, entrant)
                    }
                }

                season.entrants.each { entrant ->

                    sql = readResource("/sqlite/insert_season_entrant_constructor.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        entrant.constructors.each { constructor ->
                            ps.addBatch(season, entrant, constructor)
                        }
                    }

                    entrant.constructors.each { constructor ->

                        sql = readResource("/sqlite/insert_season_entrant_tyre_manufacturer.sql")
                        sqlite.withBatch(batchSize, sql) { ps ->
                            constructor.tyreManufacturers.each { tyreManufacturer ->
                                ps.addBatch(season, entrant, constructor, tyreManufacturer)
                            }
                        }

                        sql = readResource("/sqlite/insert_season_entrant_driver.sql")
                        sqlite.withBatch(batchSize, sql) { ps ->
                            constructor.drivers.each { driver ->
                                ps.addBatch(season, entrant, constructor, driver, [rounds: driver.rounds?.join(",")])
                            }
                        }
                    }
                }

                // Insert driver standings.

                if (season.driverStandings) {
                    sql = readResource("/sqlite/insert_season_driver_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        season.driverStandings.eachWithIndex { driverStanding, index ->
                            ps.addBatch(season, [positionDisplayOrder: index + 1], driverStanding)
                        }
                    }
                }

                // Insert constructor standings.

                if (season.constructorStandings) {
                    sql = readResource("/sqlite/insert_season_constructor_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        season.constructorStandings.eachWithIndex { constructorStanding, index ->
                            ps.addBatch(season, [positionDisplayOrder: index + 1], constructorStanding)
                        }
                    }
                }
            }

            // Insert races.

            sql = readResource("/sqlite/insert_race.sql")
            sqlite.withBatch(batchSize, sql) { ps ->
                f1db.races.each { race ->
                    ps.addBatch(race)
                }
            }

            f1db.races.each { race ->

                sql = readResource("/sqlite/insert_race_qualifying_result.sql")
                if (race.preQualifyingResults) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.preQualifyingResults.eachWithIndex { qualifyingResult, index ->
                            ps.addBatch(race, [type: "PRE_QUALIFYING_RESULT", positionDisplayOrder: index + 1], qualifyingResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_practice_result.sql")
                if (race.freePractice1Results) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.freePractice1Results.eachWithIndex { practiceResult, index ->
                            ps.addBatch(race, [type: "FREE_PRACTICE_1_RESULT", positionDisplayOrder: index + 1], practiceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_practice_result.sql")
                if (race.freePractice2Results) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.freePractice2Results.eachWithIndex { practiceResult, index ->
                            ps.addBatch(race, [type: "FREE_PRACTICE_2_RESULT", positionDisplayOrder: index + 1], practiceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_practice_result.sql")
                if (race.freePractice3Results) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.freePractice3Results.eachWithIndex { practiceResult, index ->
                            ps.addBatch(race, [type: "FREE_PRACTICE_3_RESULT", positionDisplayOrder: index + 1], practiceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_practice_result.sql")
                if (race.freePractice4Results) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.freePractice4Results.eachWithIndex { practiceResult, index ->
                            ps.addBatch(race, [type: "FREE_PRACTICE_4_RESULT", positionDisplayOrder: index + 1], practiceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_qualifying_result.sql")
                if (race.qualifying1Results) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.qualifying1Results.eachWithIndex { qualifyingResult, index ->
                            ps.addBatch(race, [type: "QUALIFYING_1_RESULT", positionDisplayOrder: index + 1], qualifyingResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_qualifying_result.sql")
                if (race.qualifying2Results) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.qualifying2Results.eachWithIndex { qualifyingResult, index ->
                            ps.addBatch(race, [type: "QUALIFYING_2_RESULT", positionDisplayOrder: index + 1], qualifyingResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_qualifying_result.sql")
                if (race.qualifyingResults) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.qualifyingResults.eachWithIndex { qualifyingResult, index ->
                            ps.addBatch(race, [type: "QUALIFYING_RESULT", positionDisplayOrder: index + 1], qualifyingResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_practice_result.sql")
                if (race.warmingUpResults) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.warmingUpResults.eachWithIndex { practiceResult, index ->
                            ps.addBatch(race, [type: "WARMING_UP_RESULT", positionDisplayOrder: index + 1], practiceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_starting_grid_position.sql")
                if (race.sprintQualifyingStartingGridPositions) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.sprintQualifyingStartingGridPositions.eachWithIndex { startingGridPosition, index ->
                            ps.addBatch(race, [type: "SPRINT_QUALIFYING_STARTING_GRID_POSITION", positionDisplayOrder: index + 1], startingGridPosition)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_race_result.sql")
                if (race.sprintQualifyingResults) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.sprintQualifyingResults.eachWithIndex { raceResult, index ->
                            ps.addBatch(race, [type: "SPRINT_QUALIFYING_RESULT", positionDisplayOrder: index + 1], raceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_starting_grid_position.sql")
                if (race.startingGridPositions) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.startingGridPositions.eachWithIndex { startingGridPosition, index ->
                            ps.addBatch(race, [type: "STARTING_GRID_POSITION", positionDisplayOrder: index + 1], startingGridPosition)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_race_result.sql")
                if (race.raceResults) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.raceResults.eachWithIndex { raceResult, index ->
                            ps.addBatch(race, [type: "RACE_RESULT", positionDisplayOrder: index + 1], raceResult)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_fastest_lap.sql")
                if (race.fastestLaps) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.fastestLaps.eachWithIndex { fastestLap, index ->
                            ps.addBatch(race, [type: "FASTEST_LAP", positionDisplayOrder: index + 1], fastestLap)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_pit_stop.sql")
                if (race.pitStops) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.pitStops.eachWithIndex { pitStop, index ->
                            ps.addBatch(race, [type: "PIT_STOP", positionDisplayOrder: index + 1], pitStop)
                        }
                    }
                }

                sql = readResource("/sqlite/insert_race_driver_of_the_day_result.sql")
                if (race.driverOfTheDayResults) {
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.driverOfTheDayResults.eachWithIndex { driverOfTheDayResult, index ->
                            ps.addBatch(race, [type: "DRIVER_OF_THE_DAY_RESULT", positionDisplayOrder: index + 1], driverOfTheDayResult)
                        }
                    }
                }

                if (race.driverStandings) {
                    sql = readResource("/sqlite/insert_race_driver_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.driverStandings.eachWithIndex { driverStanding, index ->
                            ps.addBatch(race, [round: race.round, positionDisplayOrder: index + 1], driverStanding)
                        }
                    }
                }

                if (race.constructorStandings) {
                    sql = readResource("/sqlite/insert_race_constructor_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.constructorStandings.eachWithIndex { constructorStanding, index ->
                            ps.addBatch(race, [round: race.round, positionDisplayOrder: index + 1], constructorStanding)
                        }
                    }
                }
            }

            // Rebuild the database; repacking it into a minimal amount of disk space.

            sqlite.execute("VACUUM;")
        }
    }

    /**
     * Read the content from a resource from the classpath.
     */
    private String readResource(String resource, String charset = StandardCharsets.UTF_8.toString()) {
        return getClass().getResource(resource).getText(charset)
    }

    private CsvMapper createCsvMapper() {
        def mapper = new CsvMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        mapper.configure(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS, true)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModules(new JavaTimeModule(), createSplittedModule())
        return mapper
    }

    private ObjectMapper createJsonMapper() {
        def mapper = new ObjectMapper()
        mapper.defaultPrettyPrinter = new DefaultPrettyPrinter().withArrayIndenter(new DefaultPrettyPrinter.NopIndenter()).withoutSpacesInObjectEntries()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        mapper.configure(SerializationFeature.INDENT_OUTPUT, indentOutput)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(new JavaTimeModule())
        return mapper
    }

    private SmileMapper createSmileMapper() {
        def mapper = new SmileMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(new JavaTimeModule())
        return mapper
    }

    private SimpleModule createSplittedModule() {
        def module = new SimpleModule()
        module.setMixInAnnotation(Driver, SplittedDriverMixIn)
        module.setMixInAnnotation(Constructor, SplittedConstructorMixIn)
        module.setMixInAnnotation(Season, SplittedSeasonMixIn)
        module.setMixInAnnotation(SplittedSeasonEntrant, SplittedSeasonEntrantMixIn)
        module.setMixInAnnotation(SplittedSeasonEntrantConstructor, SplittedSeasonEntrantConstructorMixIn)
        module.setMixInAnnotation(Race, SplittedRaceMixIn)
        return module
    }

    private List<SplittedDriverFamilyRelationship> getSplittedDriversFamilyRelationships() {
        return f1db.drivers.collectMany { driver ->
            driver.familyRelationships.collect {
                new SplittedDriverFamilyRelationship(driver, it)
            }
        }
    }

    private List<SplittedPreviousNextConstructor> getSplittedConstructorsPreviousNextConstructors() {
        return f1db.constructors.collectMany { constructor ->
            constructor.previousNextConstructors.collect {
                new SplittedPreviousNextConstructor(constructor, it)
            }
        }
    }

    private List<SplittedSeasonEntrant> getSplittedSeasonsEntrants() {
        return f1db.seasons.collectMany { season ->
            season.entrants.collect {
                new SplittedSeasonEntrant(season, it)
            }
        }
    }

    private List<SplittedSeasonEntrantConstructor> getSplittedSeasonsEntrantsConstructors() {
        return f1db.seasons.collectMany { season ->
            season.entrants.collectMany { seasonEntrant ->
                seasonEntrant.constructors.collect {
                    new SplittedSeasonEntrantConstructor(season, seasonEntrant, it)
                }
            }
        }
    }

    private List<SplittedSeasonEntrantTyreManufacturer> getSplittedSeasonsEntrantsTyreManufacturers() {
        return f1db.seasons.collectMany { season ->
            season.entrants.collectMany { seasonEntrant ->
                seasonEntrant.constructors.collectMany { seasonEntrantConstructor ->
                    seasonEntrantConstructor.tyreManufacturers.collect {
                        new SplittedSeasonEntrantTyreManufacturer(season, seasonEntrant, seasonEntrantConstructor, it)
                    }
                }
            }
        }
    }

    private List<SplittedSeasonEntrantDriver> getSplittedSeasonsEntrantsDrivers() {
        return f1db.seasons.collectMany { season ->
            season.entrants.collectMany { seasonEntrant ->
                seasonEntrant.constructors.collectMany { seasonEntrantConstructor ->
                    seasonEntrantConstructor.drivers.collect {
                        new SplittedSeasonEntrantDriver(season, seasonEntrant, seasonEntrantConstructor, it)
                    }
                }
            }
        }
    }

    private List<SplittedSeasonDriverStanding> getSplittedSeasonsDriverStandings() {
        return f1db.seasons.collectMany { season ->
            season.driverStandings?.withIndex().collect { SeasonDriverStanding entry, int index ->
                new SplittedSeasonDriverStanding(season, index + 1, entry)
            }
        }
    }

    private List<SplittedSeasonConstructorStanding> getSplittedSeasonsConstructorStandings() {
        return f1db.seasons.collectMany { season ->
            season.constructorStandings?.withIndex().collect { SeasonConstructorStanding entry, int index ->
                new SplittedSeasonConstructorStanding(season, index + 1, entry)
            }
        }
    }

    private List<SplittedQualifyingResult> getSplittedRacesPreQualifyingResults() {
        return f1db.races.collectMany { race ->
            race.preQualifyingResults?.withIndex().collect { QualifyingResult entry, int index ->
                new SplittedQualifyingResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedPracticeResult> getSplittedRacesFreePractice1Results() {
        return f1db.races.collectMany { race ->
            race.freePractice1Results?.withIndex().collect { PracticeResult entry, int index ->
                new SplittedPracticeResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedPracticeResult> getSplittedRacesFreePractice2Results() {
        return f1db.races.collectMany { race ->
            race.freePractice2Results?.withIndex().collect { PracticeResult entry, int index ->
                new SplittedPracticeResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedPracticeResult> getSplittedRacesFreePractice3Results() {
        return f1db.races.collectMany { race ->
            race.freePractice3Results?.withIndex().collect { PracticeResult entry, int index ->
                new SplittedPracticeResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedPracticeResult> getSplittedRacesFreePractice4Results() {
        return f1db.races.collectMany { race ->
            race.freePractice4Results?.withIndex().collect { PracticeResult entry, int index ->
                new SplittedPracticeResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedQualifyingResult> getSplittedRacesQualifying1Results() {
        return f1db.races.collectMany { race ->
            race.qualifying1Results?.withIndex().collect { QualifyingResult entry, int index ->
                new SplittedQualifyingResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedQualifyingResult> getSplittedRacesQualifying2Results() {
        return f1db.races.collectMany { race ->
            race.qualifying2Results?.withIndex().collect { QualifyingResult entry, int index ->
                new SplittedQualifyingResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedQualifyingResult> getSplittedRacesQualifyingResults() {
        return f1db.races.collectMany { race ->
            race.qualifyingResults?.withIndex().collect { QualifyingResult entry, int index ->
                new SplittedQualifyingResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedStartingGridPosition> getSplittedRacesSprintQualifyingStartingGridPositions() {
        return f1db.races.collectMany { race ->
            race.sprintQualifyingStartingGridPositions?.withIndex().collect { StartingGridPosition entry, int index ->
                new SplittedStartingGridPosition(race, index + 1, entry)
            }
        }
    }

    private List<SplittedRaceResult> getSplittedRacesSprintQualifyingResults() {
        return f1db.races.collectMany { race ->
            race.sprintQualifyingResults?.withIndex().collect { RaceResult entry, int index ->
                new SplittedRaceResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedPracticeResult> getSplittedRacesWarmingUpResults() {
        return f1db.races.collectMany { race ->
            race.warmingUpResults?.withIndex().collect { PracticeResult entry, int index ->
                new SplittedPracticeResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedStartingGridPosition> getSplittedRacesStartingGridPositions() {
        return f1db.races.collectMany { race ->
            race.startingGridPositions?.withIndex().collect { StartingGridPosition entry, int index ->
                new SplittedStartingGridPosition(race, index + 1, entry)
            }
        }
    }

    private List<SplittedRaceResult> getSplittedRacesRaceResults() {
        return f1db.races.collectMany { race ->
            race.raceResults?.withIndex().collect { RaceResult entry, int index ->
                new SplittedRaceResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedFastestLap> getSplittedRacesFastestLaps() {
        return f1db.races.collectMany { race ->
            race.fastestLaps?.withIndex().collect { FastestLap entry, int index ->
                new SplittedFastestLap(race, index + 1, entry)
            }
        }
    }

    private List<SplittedPitStop> getSplittedRacesPitStops() {
        return f1db.races.collectMany { race ->
            race.pitStops?.withIndex().collect { PitStop entry, int index ->
                new SplittedPitStop(race, index + 1, entry)
            }
        }
    }

    private List<SplittedDriverOfTheDayResult> getSplittedRacesDriverOfTheDayResults() {
        return f1db.races.collectMany { race ->
            race.driverOfTheDayResults?.withIndex().collect { DriverOfTheDayResult entry, int index ->
                new SplittedDriverOfTheDayResult(race, index + 1, entry)
            }
        }
    }

    private List<SplittedRaceDriverStanding> getSplittedRacesDriverStandings() {
        return f1db.races.collectMany { race ->
            race.driverStandings?.withIndex().collect { RaceDriverStanding entry, int index ->
                new SplittedRaceDriverStanding(race, index + 1, entry)
            }
        }
    }

    private List<SplittedRaceConstructorStanding> getSplittedRacesConstructorStandings() {
        return f1db.races.collectMany { race ->
            race.constructorStandings?.withIndex().collect { RaceConstructorStanding entry, int index ->
                new SplittedRaceConstructorStanding(race, index + 1, entry)
            }
        }
    }
}
