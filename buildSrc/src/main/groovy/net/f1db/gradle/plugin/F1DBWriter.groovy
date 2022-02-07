package net.f1db.gradle.plugin

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.smile.SmileFactory
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import groovy.sql.Sql
import net.f1db.F1db
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

    File jsonSchemaFile
    File outputDir
    boolean indentOutput

    F1DBWriter(File projectDir, F1DBPluginExtension extension) {
        this.jsonSchemaFile = new File(projectDir, "src/schema/f1db.schema.latest.json")
        this.outputDir = new File(projectDir, "build/data")
        this.indentOutput = extension.indentOutput
    }

    void write(F1db f1db) {

        println "Writing data........."

        outputDir.deleteDir()
        outputDir.mkdirs()

        writeJson(f1db)
        writeSmile(f1db)
        writeSQLiteDatabase(f1db)
    }

    private void writeJson(F1db f1db) {

        println "Writing f1db.json...."

        def outputFile = new File(outputDir, "f1db.json")

        // Create the json mapper.

        def mapper = new ObjectMapper()
        mapper.defaultPrettyPrinter = new DefaultPrettyPrinter().withArrayIndenter(new DefaultPrettyPrinter.NopIndenter()).withoutSpacesInObjectEntries()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        mapper.configure(SerializationFeature.INDENT_OUTPUT, indentOutput)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(new JavaTimeModule())

        // Write the json file.

        mapper.writeValue(outputFile, f1db)

        // Validate the json file.

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

    private void writeSmile(F1db f1db) {

        println "Writing f1db.sml....."

        def outputFile = new File(outputDir, "f1db.sml")

        // Create the smile mapper.

        def mapper = new SmileMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(new JavaTimeModule())

        // Write the smile file.

        mapper.writeValue(outputFile, f1db)

        // Validate the smile file.

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

    private void writeSQLiteDatabase(F1db f1db) {

        println "Writing f1db.db......"

        def outputFile = new File(outputDir, "f1db.db")

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
                    sql = readResource("/sqlite/insert_driver_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        season.driverStandings.eachWithIndex { driverStanding, index ->
                            ps.addBatch(season, [round: 0, positionDisplayOrder: index + 1], driverStanding)
                        }
                    }
                }

                // Insert constructor standings.

                if (season.constructorStandings) {
                    sql = readResource("/sqlite/insert_constructor_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        season.constructorStandings.eachWithIndex { constructorStanding, index ->
                            ps.addBatch(season, [round: 0, positionDisplayOrder: index + 1], constructorStanding)
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
                    sql = readResource("/sqlite/insert_driver_standing.sql")
                    sqlite.withBatch(batchSize, sql) { ps ->
                        race.driverStandings.eachWithIndex { driverStanding, index ->
                            ps.addBatch(race, [round: race.round, positionDisplayOrder: index + 1], driverStanding)
                        }
                    }
                }

                if (race.constructorStandings) {
                    sql = readResource("/sqlite/insert_constructor_standing.sql")
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
}
