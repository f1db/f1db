package com.f1db.plugin.writer.csv

import com.f1db.plugin.extensions.splitted
import com.f1db.plugin.schema.single.F1db
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.dataformat.csv.CsvGenerator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.io.File

/**
 * The CSV writer.
 *
 * @author Marcel Overdijk
 */
class CsvWriter(
        private val outputDir: File,
        private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        // Write splitted files.

        write("f1db-drivers.csv", db.splitted.drivers)
        write("f1db-drivers-family-relationships.csv", db.splitted.driverFamilyRelationships)
        write("f1db-constructors.csv", db.splitted.constructors)
        write("f1db-constructors-chronology.csv", db.splitted.constructorChronology)
        write("f1db-chassis.csv", db.splitted.chassis)
        write("f1db-engine-manufacturers.csv", db.splitted.engineManufacturers)
        write("f1db-engines.csv", db.splitted.engines)
        write("f1db-tyre-manufacturers.csv", db.splitted.tyreManufacturers)
        write("f1db-entrants.csv", db.splitted.entrants)
        write("f1db-circuits.csv", db.splitted.circuits)
        write("f1db-grands-prix.csv", db.splitted.grandsPrix)
        write("f1db-seasons.csv", db.splitted.seasons)
        write("f1db-seasons-entrants.csv", db.splitted.seasonEntrants)
        write("f1db-seasons-entrants-constructors.csv", db.splitted.seasonEntrantConstructors)
        write("f1db-seasons-entrants-chassis.csv", db.splitted.seasonEntrantChassis)
        write("f1db-seasons-entrants-engines.csv", db.splitted.seasonEntrantEngines)
        write("f1db-seasons-entrants-tyre-manufacturers.csv", db.splitted.seasonEntrantTyreManufacturers)
        write("f1db-seasons-entrants-drivers.csv", db.splitted.seasonEntrantDrivers)
        write("f1db-seasons-constructors.csv", db.splitted.seasonConstructors)
        write("f1db-seasons-engine-manufacturers.csv", db.splitted.seasonEngineManufacturers)
        write("f1db-seasons-tyre-manufacturers.csv", db.splitted.seasonTyreManufacturers)
        write("f1db-seasons-drivers.csv", db.splitted.seasonDrivers)
        write("f1db-seasons-driver-standings.csv", db.splitted.seasonDriverStandings)
        write("f1db-seasons-constructor-standings.csv", db.splitted.seasonConstructorStandings)
        write("f1db-races.csv", db.splitted.races)
        write("f1db-races-pre-qualifying-results.csv", db.splitted.racePreQualifyingResults)
        write("f1db-races-free-practice-1-results.csv", db.splitted.raceFreePractice1Results)
        write("f1db-races-free-practice-2-results.csv", db.splitted.raceFreePractice2Results)
        write("f1db-races-free-practice-3-results.csv", db.splitted.raceFreePractice3Results)
        write("f1db-races-free-practice-4-results.csv", db.splitted.raceFreePractice4Results)
        write("f1db-races-qualifying-1-results.csv", db.splitted.raceQualifying1Results)
        write("f1db-races-qualifying-2-results.csv", db.splitted.raceQualifying2Results)
        write("f1db-races-qualifying-results.csv", db.splitted.raceQualifyingResults)
        write("f1db-races-sprint-qualifying-results.csv", db.splitted.raceSprintQualifyingResults)
        write("f1db-races-sprint-starting-grid-positions.csv", db.splitted.raceSprintStartingGridPositions)
        write("f1db-races-sprint-race-results.csv", db.splitted.raceSprintRaceResults)
        write("f1db-races-warming-up-results.csv", db.splitted.raceWarmingUpResults)
        write("f1db-races-starting-grid-positions.csv", db.splitted.raceStartingGridPositions)
        write("f1db-races-race-results.csv", db.splitted.raceRaceResults)
        write("f1db-races-fastest-laps.csv", db.splitted.raceFastestLaps)
        write("f1db-races-pit-stops.csv", db.splitted.racePitStops)
        write("f1db-races-driver-of-the-day-results.csv", db.splitted.raceDriverOfTheDayResults)
        write("f1db-races-driver-standings.csv", db.splitted.raceDriverStandings)
        write("f1db-races-constructor-standings.csv", db.splitted.raceConstructorStandings)
        write("f1db-continents.csv", db.splitted.continents)
        write("f1db-countries.csv", db.splitted.countries)
    }

    private fun write(outputFileName: String, values: List<Any>) {

        val outputFile = File(outputDir, outputFileName)

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
}
