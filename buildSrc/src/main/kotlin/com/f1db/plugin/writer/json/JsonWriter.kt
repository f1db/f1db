package com.f1db.plugin.writer.json

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.single.F1db
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import java.io.File
import java.io.FileReader

/**
 * The JSON writer.
 *
 * @author Marcel Overdijk
 */
class JsonWriter(
    private val projectName: String,
    private val outputDir: File,
    private val schemaDir: File,
    private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        // Write single file.

        write("$projectName.json", "single/$projectName.schema.json", db)

        // Write splitted files.

        write("$projectName-drivers.json", "splitted/$projectName-drivers.schema.json", db.splitted.drivers)
        write("$projectName-drivers-family-relationships.json", "splitted/$projectName-drivers-family-relationships.schema.json", db.splitted.driverFamilyRelationships)
        write("$projectName-constructors.json", "splitted/$projectName-constructors.schema.json", db.splitted.constructors)
        write("$projectName-constructors-chronology.json", "splitted/$projectName-constructors-chronology.schema.json", db.splitted.constructorChronology)
        write("$projectName-chassis.json", "splitted/$projectName-chassis.schema.json", db.splitted.chassis)
        write("$projectName-engine-manufacturers.json", "splitted/$projectName-engine-manufacturers.schema.json", db.splitted.engineManufacturers)
        write("$projectName-engines.json", "splitted/$projectName-engines.schema.json", db.splitted.engines)
        write("$projectName-tyre-manufacturers.json", "splitted/$projectName-tyre-manufacturers.schema.json", db.splitted.tyreManufacturers)
        write("$projectName-entrants.json", "splitted/$projectName-entrants.schema.json", db.splitted.entrants)
        write("$projectName-circuits.json", "splitted/$projectName-circuits.schema.json", db.splitted.circuits)
        write("$projectName-grands-prix.json", "splitted/$projectName-grands-prix.schema.json", db.splitted.grandsPrix)
        write("$projectName-seasons.json", "splitted/$projectName-seasons.schema.json", db.splitted.seasons)
        write("$projectName-seasons-entrants.json", "splitted/$projectName-seasons-entrants.schema.json", db.splitted.seasonEntrants)
        write("$projectName-seasons-entrants-constructors.json", "splitted/$projectName-seasons-entrants-constructors.schema.json", db.splitted.seasonEntrantConstructors)
        write("$projectName-seasons-entrants-chassis.json", "splitted/$projectName-seasons-entrants-chassis.schema.json", db.splitted.seasonEntrantChassis)
        write("$projectName-seasons-entrants-engines.json", "splitted/$projectName-seasons-entrants-engines.schema.json", db.splitted.seasonEntrantEngines)
        write("$projectName-seasons-entrants-tyre-manufacturers.json", "splitted/$projectName-seasons-entrants-tyre-manufacturers.schema.json", db.splitted.seasonEntrantTyreManufacturers)
        write("$projectName-seasons-entrants-drivers.json", "splitted/$projectName-seasons-entrants-drivers.schema.json", db.splitted.seasonEntrantDrivers)
        write("$projectName-seasons-constructors.json", "splitted/$projectName-seasons-constructors.schema.json", db.splitted.seasonConstructors)
        write("$projectName-seasons-engine-manufacturers.json", "splitted/$projectName-seasons-engine-manufacturers.schema.json", db.splitted.seasonEngineManufacturers)
        write("$projectName-seasons-tyre-manufacturers.json", "splitted/$projectName-seasons-tyre-manufacturers.schema.json", db.splitted.seasonTyreManufacturers)
        write("$projectName-seasons-drivers.json", "splitted/$projectName-seasons-drivers.schema.json", db.splitted.seasonDrivers)
        write("$projectName-seasons-driver-standings.json", "splitted/$projectName-seasons-driver-standings.schema.json", db.splitted.seasonDriverStandings)
        write("$projectName-seasons-constructor-standings.json", "splitted/$projectName-seasons-constructor-standings.schema.json", db.splitted.seasonConstructorStandings)
        write("$projectName-races.json", "splitted/$projectName-races.schema.json", db.splitted.races)
        write("$projectName-races-pre-qualifying-results.json", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.racePreQualifyingResults)
        write("$projectName-races-free-practice-1-results.json", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice1Results)
        write("$projectName-races-free-practice-2-results.json", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice2Results)
        write("$projectName-races-free-practice-3-results.json", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice3Results)
        write("$projectName-races-free-practice-4-results.json", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceFreePractice4Results)
        write("$projectName-races-qualifying-1-results.json", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifying1Results)
        write("$projectName-races-qualifying-2-results.json", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifying2Results)
        write("$projectName-races-qualifying-results.json", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceQualifyingResults)
        write("$projectName-races-sprint-qualifying-results.json", "splitted/$projectName-races-qualifying-results.schema.json", db.splitted.raceSprintQualifyingResults)
        write("$projectName-races-sprint-starting-grid-positions.json", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceSprintStartingGridPositions)
        write("$projectName-races-sprint-race-results.json", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceSprintRaceResults)
        write("$projectName-races-warming-up-results.json", "splitted/$projectName-races-practice-results.schema.json", db.splitted.raceWarmingUpResults)
        write("$projectName-races-starting-grid-positions.json", "splitted/$projectName-races-starting-grid-positions.schema.json", db.splitted.raceStartingGridPositions)
        write("$projectName-races-race-results.json", "splitted/$projectName-races-race-results.schema.json", db.splitted.raceRaceResults)
        write("$projectName-races-fastest-laps.json", "splitted/$projectName-races-fastest-laps.schema.json", db.splitted.raceFastestLaps)
        write("$projectName-races-pit-stops.json", "splitted/$projectName-races-pit-stops.schema.json", db.splitted.racePitStops)
        write("$projectName-races-driver-of-the-day-results.json", "splitted/$projectName-races-driver-of-the-day-results.schema.json", db.splitted.raceDriverOfTheDayResults)
        write("$projectName-races-driver-standings.json", "splitted/$projectName-races-driver-standings.schema.json", db.splitted.raceDriverStandings)
        write("$projectName-races-constructor-standings.json", "splitted/$projectName-races-constructor-standings.schema.json", db.splitted.raceConstructorStandings)
        write("$projectName-continents.json", "splitted/$projectName-continents.schema.json", db.splitted.continents)
        write("$projectName-countries.json", "splitted/$projectName-countries.schema.json", db.splitted.countries)
    }

    private fun write(outputFileName: String, schemaFileName: String, value: Any) {

        val outputFile = File(outputDir, outputFileName)
        val schemaFile = File(schemaDir, schemaFileName)

        println("Writing ${outputFile.name}....")

        // Create the json mapper.

        val mapper = JsonMapper.builder()
            .defaultPrettyPrinter(DefaultPrettyPrinter().withArrayIndenter(DefaultPrettyPrinter.NopIndenter()))
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
}
