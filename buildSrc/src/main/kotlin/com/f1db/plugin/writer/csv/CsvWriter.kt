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
    private val projectName: String,
    private val outputDir: File,
    private val db: F1db,
) {

    fun write() {

        outputDir.mkdirs()

        // Write splitted files.

        write("$projectName-drivers.csv", db.splitted.drivers)
        write("$projectName-drivers-family-relationships.csv", db.splitted.driverFamilyRelationships)
        write("$projectName-constructors.csv", db.splitted.constructors)
        write("$projectName-constructors-previous-next-constructors.csv", db.splitted.constructorPreviousNextConstructors)
        write("$projectName-engine-manufacturers.csv", db.splitted.engineManufacturers)
        write("$projectName-tyre-manufacturers.csv", db.splitted.tyreManufacturers)
        write("$projectName-entrants.csv", db.splitted.entrants)
        write("$projectName-circuits.csv", db.splitted.circuits)
        write("$projectName-grands-prix.csv", db.splitted.grandsPrix)
        write("$projectName-seasons.csv", db.splitted.seasons)
        write("$projectName-seasons-entrants.csv", db.splitted.seasonEntrants)
        write("$projectName-seasons-entrants-constructors.csv", db.splitted.seasonEntrantConstructors)
        write("$projectName-seasons-entrants-tyre-manufacturers.csv", db.splitted.seasonEntrantTyreManufacturers)
        write("$projectName-seasons-entrants-drivers.csv", db.splitted.seasonEntrantDrivers)
        write("$projectName-seasons-driver-standings.csv", db.splitted.seasonDriverStandings)
        write("$projectName-seasons-constructor-standings.csv", db.splitted.seasonConstructorStandings)
        write("$projectName-races.csv", db.splitted.races)
        write("$projectName-races-pre-qualifying-results.csv", db.splitted.racePreQualifyingResults)
        write("$projectName-races-free-practice-1-results.csv", db.splitted.raceFreePractice1Results)
        write("$projectName-races-free-practice-2-results.csv", db.splitted.raceFreePractice2Results)
        write("$projectName-races-free-practice-3-results.csv", db.splitted.raceFreePractice3Results)
        write("$projectName-races-free-practice-4-results.csv", db.splitted.raceFreePractice4Results)
        write("$projectName-races-qualifying-1-results.csv", db.splitted.raceQualifying1Results)
        write("$projectName-races-qualifying-2-results.csv", db.splitted.raceQualifying2Results)
        write("$projectName-races-qualifying-results.csv", db.splitted.raceQualifyingResults)
        write("$projectName-races-sprint-qualifying-results.csv", db.splitted.raceSprintQualifyingResults)
        write("$projectName-races-sprint-starting-grid-positions.csv", db.splitted.raceSprintStartingGridPositions)
        write("$projectName-races-sprint-race-results.csv", db.splitted.raceSprintRaceResults)
        write("$projectName-races-warming-up-results.csv", db.splitted.raceWarmingUpResults)
        write("$projectName-races-starting-grid-positions.csv", db.splitted.raceStartingGridPositions)
        write("$projectName-races-race-results.csv", db.splitted.raceRaceResults)
        write("$projectName-races-fastest-laps.csv", db.splitted.raceFastestLaps)
        write("$projectName-races-pit-stops.csv", db.splitted.racePitStops)
        write("$projectName-races-driver-of-the-day-results.csv", db.splitted.raceDriverOfTheDayResults)
        write("$projectName-races-driver-standings.csv", db.splitted.raceDriverStandings)
        write("$projectName-races-constructor-standings.csv", db.splitted.raceConstructorStandings)
        write("$projectName-continents.csv", db.splitted.continents)
        write("$projectName-countries.csv", db.splitted.countries)
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
