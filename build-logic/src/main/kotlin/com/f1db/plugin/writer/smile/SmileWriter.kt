package com.f1db.plugin.writer.smile

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.single.F1db
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

/**
 * The Smile writer.
 *
 * @author Marcel Overdijk
 */
class SmileWriter(
        private val outputDir: File,
        private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        // Write single file.

        write("f1db.sml", db)

        // Write splitted files.

        write("f1db-drivers.sml", db.splitted.drivers)
        write("f1db-drivers-family-relationships.sml", db.splitted.driverFamilyRelationships)
        write("f1db-constructors.sml", db.splitted.constructors)
        write("f1db-constructors-chronology.sml", db.splitted.constructorChronology)
        write("f1db-chassis.sml", db.splitted.chassis)
        write("f1db-engine-manufacturers.sml", db.splitted.engineManufacturers)
        write("f1db-engines.sml", db.splitted.engines)
        write("f1db-tyre-manufacturers.sml", db.splitted.tyreManufacturers)
        write("f1db-entrants.sml", db.splitted.entrants)
        write("f1db-circuits.sml", db.splitted.circuits)
        write("f1db-grands-prix.sml", db.splitted.grandsPrix)
        write("f1db-seasons.sml", db.splitted.seasons)
        write("f1db-seasons-entrants.sml", db.splitted.seasonEntrants)
        write("f1db-seasons-entrants-constructors.sml", db.splitted.seasonEntrantConstructors)
        write("f1db-seasons-entrants-chassis.sml", db.splitted.seasonEntrantChassis)
        write("f1db-seasons-entrants-engines.sml", db.splitted.seasonEntrantEngines)
        write("f1db-seasons-entrants-tyre-manufacturers.sml", db.splitted.seasonEntrantTyreManufacturers)
        write("f1db-seasons-entrants-drivers.sml", db.splitted.seasonEntrantDrivers)
        write("f1db-seasons-constructors.sml", db.splitted.seasonConstructors)
        write("f1db-seasons-engine-manufacturers.sml", db.splitted.seasonEngineManufacturers)
        write("f1db-seasons-tyre-manufacturers.sml", db.splitted.seasonTyreManufacturers)
        write("f1db-seasons-drivers.sml", db.splitted.seasonDrivers)
        write("f1db-seasons-driver-standings.sml", db.splitted.seasonDriverStandings)
        write("f1db-seasons-constructor-standings.sml", db.splitted.seasonConstructorStandings)
        write("f1db-races.sml", db.splitted.races)
        write("f1db-races-pre-qualifying-results.sml", db.splitted.racePreQualifyingResults)
        write("f1db-races-free-practice-1-results.sml", db.splitted.raceFreePractice1Results)
        write("f1db-races-free-practice-2-results.sml", db.splitted.raceFreePractice2Results)
        write("f1db-races-free-practice-3-results.sml", db.splitted.raceFreePractice3Results)
        write("f1db-races-free-practice-4-results.sml", db.splitted.raceFreePractice4Results)
        write("f1db-races-qualifying-1-results.sml", db.splitted.raceQualifying1Results)
        write("f1db-races-qualifying-2-results.sml", db.splitted.raceQualifying2Results)
        write("f1db-races-qualifying-results.sml", db.splitted.raceQualifyingResults)
        write("f1db-races-sprint-qualifying-results.sml", db.splitted.raceSprintQualifyingResults)
        write("f1db-races-sprint-starting-grid-positions.sml", db.splitted.raceSprintStartingGridPositions)
        write("f1db-races-sprint-race-results.sml", db.splitted.raceSprintRaceResults)
        write("f1db-races-warming-up-results.sml", db.splitted.raceWarmingUpResults)
        write("f1db-races-starting-grid-positions.sml", db.splitted.raceStartingGridPositions)
        write("f1db-races-race-results.sml", db.splitted.raceRaceResults)
        write("f1db-races-fastest-laps.sml", db.splitted.raceFastestLaps)
        write("f1db-races-pit-stops.sml", db.splitted.racePitStops)
        write("f1db-races-driver-of-the-day-results.sml", db.splitted.raceDriverOfTheDayResults)
        write("f1db-races-driver-standings.sml", db.splitted.raceDriverStandings)
        write("f1db-races-constructor-standings.sml", db.splitted.raceConstructorStandings)
        write("f1db-continents.sml", db.splitted.continents)
        write("f1db-countries.sml", db.splitted.countries)
    }

    private fun write(outputFileName: String, value: Any) {

        val outputFile = File(outputDir, outputFileName)

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
    }
}
