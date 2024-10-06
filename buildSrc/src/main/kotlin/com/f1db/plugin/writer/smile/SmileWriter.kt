package com.f1db.plugin.writer.smile

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.single.F1db
import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.smile.SmileFactory
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader

/**
 * The Smile writer.
 *
 * @author Marcel Overdijk
 */
class SmileWriter(
    private val projectName: String,
    private val outputDir: File,
    private val schemaDir: File,
    private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        // Write single file.

        write("$projectName.sml", "single/$projectName.schema.json", db)

        // Write splitted files.

        write("$projectName-drivers.sml", "splitted/$projectName-drivers.schema.json", db.splitted.drivers)
        write("$projectName-drivers-family-relationships.sml", "splitted/$projectName-drivers-family-relationships.schema.json", db.splitted.driverFamilyRelationships)
        write("$projectName-constructors.sml", "splitted/$projectName-constructors.schema.json", db.splitted.constructors)
        write("$projectName-constructors-chronology.sml", "splitted/$projectName-constructors-chronology.schema.json", db.splitted.constructorChronology)
        write("$projectName-chassis.sml", "splitted/$projectName-chassis.schema.json", db.splitted.chassis)
        write("$projectName-engine-manufacturers.sml", "splitted/$projectName-engine-manufacturers.schema.json", db.splitted.engineManufacturers)
        write("$projectName-engines.sml", "splitted/$projectName-engines.schema.json", db.splitted.engines)
        write("$projectName-tyre-manufacturers.sml", "splitted/$projectName-tyre-manufacturers.schema.json", db.splitted.tyreManufacturers)
        write("$projectName-entrants.sml", "splitted/$projectName-entrants.schema.json", db.splitted.entrants)
        write("$projectName-circuits.sml", "splitted/$projectName-circuits.schema.json", db.splitted.circuits)
        write("$projectName-grands-prix.sml", "splitted/$projectName-grands-prix.schema.json", db.splitted.grandsPrix)
        write("$projectName-seasons.sml", "splitted/$projectName-seasons.schema.json", db.splitted.seasons)
        write("$projectName-seasons-entrants.sml", "splitted/$projectName-seasons-entrants.schema.json", db.splitted.seasonEntrants)
        write("$projectName-seasons-entrants-constructors.sml", "splitted/$projectName-seasons-entrants-constructors.schema.json", db.splitted.seasonEntrantConstructors)
        write("$projectName-seasons-entrants-chassis.sml", "splitted/$projectName-seasons-entrants-chassis.schema.json", db.splitted.seasonEntrantChassis)
        write("$projectName-seasons-entrants-engines.sml", "splitted/$projectName-seasons-entrants-engines.schema.json", db.splitted.seasonEntrantEngines)
        write("$projectName-seasons-entrants-tyre-manufacturers.sml", "splitted/$projectName-seasons-entrants-tyre-manufacturers.schema.json", db.splitted.seasonEntrantTyreManufacturers)
        write("$projectName-seasons-entrants-drivers.sml", "splitted/$projectName-seasons-entrants-drivers.schema.json", db.splitted.seasonEntrantDrivers)
        write("$projectName-seasons-constructors.sml", "splitted/$projectName-seasons-constructors.schema.json", db.splitted.seasonConstructors)
        write("$projectName-seasons-engine-manufacturers.sml", "splitted/$projectName-seasons-engine-manufacturers.schema.json", db.splitted.seasonEngineManufacturers)
        write("$projectName-seasons-tyre-manufacturers.sml", "splitted/$projectName-seasons-tyre-manufacturers.schema.json", db.splitted.seasonTyreManufacturers)
        write("$projectName-seasons-drivers.sml", "splitted/$projectName-seasons-drivers.schema.json", db.splitted.seasonDrivers)
        write("$projectName-seasons-driver-standings.sml", "splitted/$projectName-seasons-driver-standings.schema.json", db.splitted.seasonDriverStandings)
        write("$projectName-seasons-constructor-standings.sml", "splitted/$projectName-seasons-constructor-standings.schema.json", db.splitted.seasonConstructorStandings)
        write("$projectName-races.sml", "splitted/$projectName-races.schema.json", db.splitted.races)
        write("$projectName-races-pre-qualifying-results.sml", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.racePreQualifyingResults)
        write("$projectName-races-free-practice-1-results.sml", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice1Results)
        write("$projectName-races-free-practice-2-results.sml", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice2Results)
        write("$projectName-races-free-practice-3-results.sml", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice3Results)
        write("$projectName-races-free-practice-4-results.sml", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice4Results)
        write("$projectName-races-qualifying-1-results.sml", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifying1Results)
        write("$projectName-races-qualifying-2-results.sml", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifying2Results)
        write("$projectName-races-qualifying-results.sml", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifyingResults)
        write("$projectName-races-sprint-qualifying-results.sml", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceSprintQualifyingResults)
        write("$projectName-races-sprint-starting-grid-positions.sml", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceSprintStartingGridPositions)
        write("$projectName-races-sprint-race-results.sml", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceSprintRaceResults)
        write("$projectName-races-warming-up-results.sml", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceWarmingUpResults)
        write("$projectName-races-starting-grid-positions.sml", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceStartingGridPositions)
        write("$projectName-races-race-results.sml", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceRaceResults)
        write("$projectName-races-fastest-laps.sml", "splitted/$projectName-races-fastest-laps.schema.json", db.splitted.raceFastestLaps)
        write("$projectName-races-pit-stops.sml", "splitted/$projectName-races-pit-stops.schema.json", db.splitted.racePitStops)
        write("$projectName-races-driver-of-the-day-results.sml", "splitted/$projectName-races-driver-of-the-day-results.schema.json", db.splitted.raceDriverOfTheDayResults)
        write("$projectName-races-driver-standings.sml", "splitted/$projectName-races-driver-standings.schema.json", db.splitted.raceDriverStandings)
        write("$projectName-races-constructor-standings.sml", "splitted/$projectName-races-constructor-standings.schema.json", db.splitted.raceConstructorStandings)
        write("$projectName-continents.sml", "splitted/$projectName-continents.schema.json", db.splitted.continents)
        write("$projectName-countries.sml", "splitted/$projectName-countries.schema.json", db.splitted.countries)
    }

    private fun write(outputFileName: String, schemaFileName: String, value: Any) {

        val outputFile = File(outputDir, outputFileName)
        val schemaFile = File(schemaDir, schemaFileName)

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
}
