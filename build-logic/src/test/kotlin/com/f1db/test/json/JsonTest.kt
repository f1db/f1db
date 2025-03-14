package com.f1db.test.json

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import java.io.File
import java.io.FileReader
import java.util.stream.Stream

/**
 * Validates the generated JSON files.
 *
 * @author Marcel Overdijk
 */
class JsonTest {

    companion object {
        @JvmStatic
        fun splittedFiles(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("f1db-drivers.json", "f1db-drivers.schema.json"),
                Arguments.of("f1db-drivers-family-relationships.json", "f1db-drivers-family-relationships.schema.json"),
                Arguments.of("f1db-constructors.json", "f1db-constructors.schema.json"),
                Arguments.of("f1db-constructors-chronology.json", "f1db-constructors-chronology.schema.json"),
                Arguments.of("f1db-chassis.json", "f1db-chassis.schema.json"),
                Arguments.of("f1db-engine-manufacturers.json", "f1db-engine-manufacturers.schema.json"),
                Arguments.of("f1db-engines.json", "f1db-engines.schema.json"),
                Arguments.of("f1db-tyre-manufacturers.json", "f1db-tyre-manufacturers.schema.json"),
                Arguments.of("f1db-entrants.json", "f1db-entrants.schema.json"),
                Arguments.of("f1db-circuits.json", "f1db-circuits.schema.json"),
                Arguments.of("f1db-grands-prix.json", "f1db-grands-prix.schema.json"),
                Arguments.of("f1db-seasons.json", "f1db-seasons.schema.json"),
                Arguments.of("f1db-seasons-entrants.json", "f1db-seasons-entrants.schema.json"),
                Arguments.of("f1db-seasons-entrants-constructors.json", "f1db-seasons-entrants-constructors.schema.json"),
                Arguments.of("f1db-seasons-entrants-chassis.json", "f1db-seasons-entrants-chassis.schema.json"),
                Arguments.of("f1db-seasons-entrants-engines.json", "f1db-seasons-entrants-engines.schema.json"),
                Arguments.of("f1db-seasons-entrants-tyre-manufacturers.json", "f1db-seasons-entrants-tyre-manufacturers.schema.json"),
                Arguments.of("f1db-seasons-entrants-drivers.json", "f1db-seasons-entrants-drivers.schema.json"),
                Arguments.of("f1db-seasons-constructors.json", "f1db-seasons-constructors.schema.json"),
                Arguments.of("f1db-seasons-engine-manufacturers.json", "f1db-seasons-engine-manufacturers.schema.json"),
                Arguments.of("f1db-seasons-tyre-manufacturers.json", "f1db-seasons-tyre-manufacturers.schema.json"),
                Arguments.of("f1db-seasons-drivers.json", "f1db-seasons-drivers.schema.json"),
                Arguments.of("f1db-seasons-driver-standings.json", "f1db-seasons-driver-standings.schema.json"),
                Arguments.of("f1db-seasons-constructor-standings.json", "f1db-seasons-constructor-standings.schema.json"),
                Arguments.of("f1db-races.json", "f1db-races.schema.json"),
                Arguments.of("f1db-races-pre-qualifying-results.json", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-free-practice-1-results.json", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-free-practice-2-results.json", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-free-practice-3-results.json", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-free-practice-4-results.json", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-qualifying-1-results.json", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-qualifying-2-results.json", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-qualifying-results.json", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-sprint-qualifying-results.json", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-sprint-starting-grid-positions.json", "f1db-races-starting-grid-positions.schema.json"),
                Arguments.of("f1db-races-sprint-race-results.json", "f1db-races-race-results.schema.json"),
                Arguments.of("f1db-races-warming-up-results.json", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-starting-grid-positions.json", "f1db-races-starting-grid-positions.schema.json"),
                Arguments.of("f1db-races-race-results.json", "f1db-races-race-results.schema.json"),
                Arguments.of("f1db-races-fastest-laps.json", "f1db-races-fastest-laps.schema.json"),
                Arguments.of("f1db-races-pit-stops.json", "f1db-races-pit-stops.schema.json"),
                Arguments.of("f1db-races-driver-of-the-day-results.json", "f1db-races-driver-of-the-day-results.schema.json"),
                Arguments.of("f1db-races-driver-standings.json", "f1db-races-driver-standings.schema.json"),
                Arguments.of("f1db-races-constructor-standings.json", "f1db-races-constructor-standings.schema.json"),
                Arguments.of("f1db-continents.json", "f1db-continents.schema.json"),
                Arguments.of("f1db-countries.json", "f1db-countries.schema.json"),
            )
        }
    }

    @Test
    @DisplayName("test single f1db.json file")
    fun `test single f1db json file`() {
        validate("f1db.json", "single/f1db.schema.json")
    }

    @ParameterizedTest(name = "test splitted {0} file")
    @MethodSource("splittedFiles")
    fun `test splitted json file`(jsonFile: String, schemaFile: String) {
        validate(jsonFile, "splitted/$schemaFile")
    }

    private fun validate(jsonFile: String, schemaFile: String) {
        validate(File("../build/data/json/$jsonFile"), File("../src/schema/current/$schemaFile"))
    }

    private fun validate(jsonFile: File, schemaFile: File) {
        val validationService = JsonValidationService.newInstance(ClassicJsonProvider())
        val reader = FileReader(jsonFile)
        val schema = validationService.readSchema(FileReader(schemaFile))
        val handler = ProblemHandler.throwing()
        validationService.createParser(reader, schema, handler).use { parser ->
            while (parser.hasNext()) {
                parser.next()
            }
        }
    }
}
