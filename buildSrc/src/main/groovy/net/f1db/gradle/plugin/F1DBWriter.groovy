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

/**
 * The F1DB data writer.
 *
 * @author Marcel Overdijk
 */
class F1DBWriter {

    enum Format {
        CSV, JSON, SMILE
    }

    File schemaDir
    File outputDir
    File csvOutputDir
    File jsonOutputDir
    File smileOutputDir
    File sqliteOutputDir
    boolean indentOutput

    F1db f1db

    F1DBWriter(File projectDir, F1DBPluginExtension extension, F1db f1db) {
        this.schemaDir = new File(projectDir, "src/schema/v${extension.jsonSchemaVersion}")
        this.outputDir = new File(projectDir, "build/data")
        this.csvOutputDir = new File(outputDir, "csv")
        this.jsonOutputDir = new File(outputDir, "json")
        this.smileOutputDir = new File(outputDir, "smile")
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
        smileOutputDir.mkdirs()
        sqliteOutputDir.mkdirs()

        writeCsvFiles()
        writeJsonFiles()
        writeSmileFiles()
        writeSQLiteDatabase()
    }

    private void writeCsvFiles() {
        writeSplittedFiles(Format.CSV)
    }

    private void writeCsvSplittedFile(String outputFileName, List<Object> values) {

        def outputFile = new File(csvOutputDir, outputFileName)

        println "Writing ${outputFile.name}...."

        // Create the csv mapper.

        def mapper = createCsvMapper()

        // Write the csv file.

        def schemaClass = values[0].class
        def schema = mapper.schemaFor(schemaClass).withHeader()
        mapper.writer(schema).writeValue(outputFile, values)
    }

    private void writeJsonFiles() {
        writeJsonSingleFile()
        writeSplittedFiles(Format.JSON)
    }

    private void writeJsonSingleFile() {

        def outputFile = new File(jsonOutputDir, "f1db.json")

        println "Writing ${outputFile.name}...."

        // Create the json mapper.

        def mapper = createJsonMapper()

        // Write the single json file.

        mapper.writeValue(outputFile, f1db)

        // Validate the single json file.

        def schemaFile = new File(schemaDir, "f1db.json-schema.json")
        validateJsonFile(schemaFile, outputFile)
    }

    private void writeJsonSplittedFile(String outputFileName, List<Object> values, String schemaFileName) {

        def outputFile = new File(jsonOutputDir, outputFileName)

        println "Writing ${outputFile.name}...."

        // Create the json mapper.

        def mapper = createJsonMapper()
        mapper.registerModule(createSplittedModule())

        // Write the json file.

        mapper.writeValue(outputFile, values)

        // Validate the json file.

        def schemaFile = new File(schemaDir, "splitted/${schemaFileName}")
        validateJsonFile(schemaFile, outputFile)
    }

    private void validateJsonFile(File schemaFile, File outputFile) {
        def validationService = JsonValidationService.newInstance(new ClassicJsonProvider())
        def schema = validationService.readSchema(new FileReader(schemaFile))
        def reader = new FileReader(outputFile)
        def handler = ProblemHandler.throwing()
        validationService.createParser(reader, schema, handler).withCloseable { parser ->
            while (parser.hasNext()) {
                parser.next()
            }
        }
    }

    private void writeSmileFiles() {
        writeSmileSingleFile()
        writeSplittedFiles(Format.SMILE)
    }

    private writeSmileSingleFile() {

        def outputFile = new File(smileOutputDir, "f1db.sml")

        println "Writing ${outputFile.name}...."

        // Create the smile mapper.

        def mapper = createSmileMapper()

        // Write the single smile file.

        mapper.writeValue(outputFile, f1db)

        // Validate the single smile file.

        def schemaFile = new File(schemaDir, "f1db.json-schema.json")
        validateSmileFile(schemaFile, outputFile)
    }

    private void writeSmileSplittedFile(String outputFileName, List<Object> values, String schemaFileName) {

        def outputFile = new File(smileOutputDir, outputFileName)

        println "Writing ${outputFile.name}...."

        // Create the smile mapper.

        def mapper = createSmileMapper()
        mapper.registerModule(createSplittedModule())

        // Write the smile file.

        mapper.writeValue(outputFile, values)

        // Validate the smile file.

        def schemaFile = new File(schemaDir, "splitted/${schemaFileName}")
        validateSmileFile(schemaFile, outputFile)
    }

    private void validateSmileFile(File schemaFile, File outputFile) {
        def validationService = JsonValidationService.newInstance(new ClassicJsonProvider())
        def schema = validationService.readSchema(new FileReader(schemaFile))
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

    private void writeSplittedFiles(Format format) {
        writeSplittedFile(format, "f1db-continents", "f1db-continents", f1db.continents)
        writeSplittedFile(format, "f1db-countries", "f1db-countries", f1db.countries)
        writeSplittedFile(format, "f1db-drivers", "f1db-drivers", f1db.drivers)
        writeSplittedFile(format, "f1db-drivers-family-relationships", "f1db-drivers-family-relationships", getSplittedDriversFamilyRelationships())
        writeSplittedFile(format, "f1db-constructors", "f1db-constructors", f1db.constructors)
        writeSplittedFile(format, "f1db-constructors-previous-next-constructors", "f1db-constructors-previous-next-constructors", getSplittedConstructorsPreviousNextConstructors())
        writeSplittedFile(format, "f1db-engine-manufacturers", "f1db-engine-manufacturers", f1db.engineManufacturers)
        writeSplittedFile(format, "f1db-tyre-manufacturers", "f1db-tyre-manufacturers", f1db.tyreManufacturers)
        writeSplittedFile(format, "f1db-entrants", "f1db-entrants", f1db.entrants)
        writeSplittedFile(format, "f1db-circuits", "f1db-circuits", f1db.circuits)
        writeSplittedFile(format, "f1db-grands-prix", "f1db-grands-prix", f1db.grandsPrix)
        writeSplittedFile(format, "f1db-seasons", "f1db-seasons", f1db.seasons)
        writeSplittedFile(format, "f1db-seasons-entrants", "f1db-seasons-entrants", getSplittedSeasonsEntrants())
        writeSplittedFile(format, "f1db-seasons-entrants-constructors", "f1db-seasons-entrants-constructors", getSplittedSeasonsEntrantsConstructors())
        writeSplittedFile(format, "f1db-seasons-entrants-tyre-manufacturers", "f1db-seasons-entrants-tyre-manufacturers", getSplittedSeasonsEntrantsTyreManufacturers())
        writeSplittedFile(format, "f1db-seasons-entrants-drivers", "f1db-seasons-entrants-drivers", getSplittedSeasonsEntrantsDrivers())
        writeSplittedFile(format, "f1db-seasons-driver-standings", "f1db-seasons-driver-standings", getSplittedSeasonsDriverStandings())
        writeSplittedFile(format, "f1db-seasons-constructor-standings", "f1db-seasons-constructor-standings", getSplittedSeasonsConstructorStandings())
        writeSplittedFile(format, "f1db-races", "f1db-races", f1db.races)
        writeSplittedFile(format, "f1db-races-qualifying-results", "f1db-races-pre-qualifying-results", getSplittedRacesPreQualifyingResults())
        writeSplittedFile(format, "f1db-races-practice-results", "f1db-races-free-practice-1-results", getSplittedRacesFreePractice1Results())
        writeSplittedFile(format, "f1db-races-practice-results", "f1db-races-free-practice-2-results", getSplittedRacesFreePractice2Results())
        writeSplittedFile(format, "f1db-races-practice-results", "f1db-races-free-practice-3-results", getSplittedRacesFreePractice3Results())
        writeSplittedFile(format, "f1db-races-practice-results", "f1db-races-free-practice-4-results", getSplittedRacesFreePractice4Results())
        writeSplittedFile(format, "f1db-races-qualifying-results", "f1db-races-qualifying-1-results", getSplittedRacesQualifying1Results())
        writeSplittedFile(format, "f1db-races-qualifying-results", "f1db-races-qualifying-2-results", getSplittedRacesQualifying2Results())
        writeSplittedFile(format, "f1db-races-qualifying-results", "f1db-races-qualifying-results", getSplittedRacesQualifyingResults())
        writeSplittedFile(format, "f1db-races-starting-grid-positions", "f1db-races-sprint-qualifying-starting-grid-positions", getSplittedRacesSprintQualifyingStartingGridPositions())
        writeSplittedFile(format, "f1db-races-race-results", "f1db-races-sprint-qualifying-results", getSplittedRacesSprintQualifyingResults())
        writeSplittedFile(format, "f1db-races-practice-results", "f1db-races-warming-up-results", getSplittedRacesWarmingUpResults())
        writeSplittedFile(format, "f1db-races-starting-grid-positions", "f1db-races-starting-grid-positions", getSplittedRacesStartingGridPositions())
        writeSplittedFile(format, "f1db-races-race-results", "f1db-races-race-results", getSplittedRacesRaceResults())
        writeSplittedFile(format, "f1db-races-fastest-laps", "f1db-races-fastest-laps", getSplittedRacesFastestLaps())
        writeSplittedFile(format, "f1db-races-pit-stops", "f1db-races-pit-stops", getSplittedRacesPitStops())
        writeSplittedFile(format, "f1db-races-driver-of-the-day-results", "f1db-races-driver-of-the-day-results", getSplittedRacesDriverOfTheDayResults())
        writeSplittedFile(format, "f1db-races-driver-standings", "f1db-races-driver-standings", getSplittedRacesDriverStandings())
        writeSplittedFile(format, "f1db-races-constructor-standings", "f1db-races-constructor-standings", getSplittedRacesConstructorStandings())
    }

    private void writeSplittedFile(Format format, String baseSchemaFileName, String baseOutputFileName, List<Object> values) {
        switch (format) {
            case Format.CSV:
                writeCsvSplittedFile("${baseOutputFileName}.csv", values)
                break
            case Format.JSON:
                writeJsonSplittedFile("${baseOutputFileName}.json", values, "${baseSchemaFileName}.json-schema.json")
                break
            case Format.SMILE:
                writeSmileSplittedFile("${baseOutputFileName}.sml", values, "${baseSchemaFileName}.json-schema.json")
                break
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
