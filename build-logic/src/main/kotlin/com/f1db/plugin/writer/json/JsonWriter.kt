package com.f1db.plugin.writer.json

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.single.F1db
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

/**
 * The JSON writer.
 *
 * @author Marcel Overdijk
 */
class JsonWriter(
        private val outputDir: File,
        private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        // Write single file.

        write("f1db.json", db)

        // Write splitted files.

        write("f1db-drivers.json", db.splitted.drivers)
        write("f1db-drivers-family-relationships.json", db.splitted.driverFamilyRelationships)
        write("f1db-constructors.json", db.splitted.constructors)
        write("f1db-constructors-chronology.json", db.splitted.constructorChronology)
        write("f1db-chassis.json", db.splitted.chassis)
        write("f1db-engine-manufacturers.json", db.splitted.engineManufacturers)
        write("f1db-engines.json", db.splitted.engines)
        write("f1db-tyre-manufacturers.json", db.splitted.tyreManufacturers)
        write("f1db-entrants.json", db.splitted.entrants)
        write("f1db-circuits.json", db.splitted.circuits)
        write("f1db-grands-prix.json", db.splitted.grandsPrix)
        write("f1db-seasons.json", db.splitted.seasons)
        write("f1db-seasons-entrants.json", db.splitted.seasonEntrants)
        write("f1db-seasons-entrants-constructors.json", db.splitted.seasonEntrantConstructors)
        write("f1db-seasons-entrants-chassis.json", db.splitted.seasonEntrantChassis)
        write("f1db-seasons-entrants-engines.json", db.splitted.seasonEntrantEngines)
        write("f1db-seasons-entrants-tyre-manufacturers.json", db.splitted.seasonEntrantTyreManufacturers)
        write("f1db-seasons-entrants-drivers.json", db.splitted.seasonEntrantDrivers)
        write("f1db-seasons-constructors.json", db.splitted.seasonConstructors)
        write("f1db-seasons-engine-manufacturers.json", db.splitted.seasonEngineManufacturers)
        write("f1db-seasons-tyre-manufacturers.json", db.splitted.seasonTyreManufacturers)
        write("f1db-seasons-drivers.json", db.splitted.seasonDrivers)
        write("f1db-seasons-driver-standings.json", db.splitted.seasonDriverStandings)
        write("f1db-seasons-constructor-standings.json", db.splitted.seasonConstructorStandings)
        write("f1db-races.json", db.splitted.races)
        write("f1db-races-pre-qualifying-results.json", db.splitted.racePreQualifyingResults)
        write("f1db-races-free-practice-1-results.json", db.splitted.raceFreePractice1Results)
        write("f1db-races-free-practice-2-results.json", db.splitted.raceFreePractice2Results)
        write("f1db-races-free-practice-3-results.json", db.splitted.raceFreePractice3Results)
        write("f1db-races-free-practice-4-results.json", db.splitted.raceFreePractice4Results)
        write("f1db-races-qualifying-1-results.json", db.splitted.raceQualifying1Results)
        write("f1db-races-qualifying-2-results.json", db.splitted.raceQualifying2Results)
        write("f1db-races-qualifying-results.json", db.splitted.raceQualifyingResults)
        write("f1db-races-sprint-qualifying-results.json", db.splitted.raceSprintQualifyingResults)
        write("f1db-races-sprint-starting-grid-positions.json", db.splitted.raceSprintStartingGridPositions)
        write("f1db-races-sprint-race-results.json", db.splitted.raceSprintRaceResults)
        write("f1db-races-warming-up-results.json", db.splitted.raceWarmingUpResults)
        write("f1db-races-starting-grid-positions.json", db.splitted.raceStartingGridPositions)
        write("f1db-races-race-results.json", db.splitted.raceRaceResults)
        write("f1db-races-fastest-laps.json", db.splitted.raceFastestLaps)
        write("f1db-races-pit-stops.json", db.splitted.racePitStops)
        write("f1db-races-driver-of-the-day-results.json", db.splitted.raceDriverOfTheDayResults)
        write("f1db-races-driver-standings.json", db.splitted.raceDriverStandings)
        write("f1db-races-constructor-standings.json", db.splitted.raceConstructorStandings)
        write("f1db-continents.json", db.splitted.continents)
        write("f1db-countries.json", db.splitted.countries)
    }

    private fun write(outputFileName: String, value: Any) {

        val outputFile = File(outputDir, outputFileName)

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
    }
}
