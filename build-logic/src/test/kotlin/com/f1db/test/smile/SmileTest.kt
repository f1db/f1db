package com.f1db.test.smile

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.dataformat.smile.SmileFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.leadpony.joy.classic.ClassicJsonProvider
import org.leadpony.justify.api.JsonValidationService
import org.leadpony.justify.api.ProblemHandler
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.stream.Stream

/**
 * Validates the generated Smile files.
 *
 * @author Marcel Overdijk
 */
class SmileTest {

    companion object {
        @JvmStatic
        fun splittedFiles(): Stream<Arguments> {
            return Stream.of(
                Arguments.of("f1db-drivers.sml", "f1db-drivers.schema.json"),
                Arguments.of("f1db-drivers-family-relationships.sml", "f1db-drivers-family-relationships.schema.json"),
                Arguments.of("f1db-constructors.sml", "f1db-constructors.schema.json"),
                Arguments.of("f1db-constructors-chronology.sml", "f1db-constructors-chronology.schema.json"),
                Arguments.of("f1db-chassis.sml", "f1db-chassis.schema.json"),
                Arguments.of("f1db-engine-manufacturers.sml", "f1db-engine-manufacturers.schema.json"),
                Arguments.of("f1db-engines.sml", "f1db-engines.schema.json"),
                Arguments.of("f1db-tyre-manufacturers.sml", "f1db-tyre-manufacturers.schema.json"),
                Arguments.of("f1db-entrants.sml", "f1db-entrants.schema.json"),
                Arguments.of("f1db-circuits.sml", "f1db-circuits.schema.json"),
                Arguments.of("f1db-grands-prix.sml", "f1db-grands-prix.schema.json"),
                Arguments.of("f1db-seasons.sml", "f1db-seasons.schema.json"),
                Arguments.of("f1db-seasons-entrants.sml", "f1db-seasons-entrants.schema.json"),
                Arguments.of("f1db-seasons-entrants-constructors.sml", "f1db-seasons-entrants-constructors.schema.json"),
                Arguments.of("f1db-seasons-entrants-chassis.sml", "f1db-seasons-entrants-chassis.schema.json"),
                Arguments.of("f1db-seasons-entrants-engines.sml", "f1db-seasons-entrants-engines.schema.json"),
                Arguments.of("f1db-seasons-entrants-tyre-manufacturers.sml", "f1db-seasons-entrants-tyre-manufacturers.schema.json"),
                Arguments.of("f1db-seasons-entrants-drivers.sml", "f1db-seasons-entrants-drivers.schema.json"),
                Arguments.of("f1db-seasons-constructors.sml", "f1db-seasons-constructors.schema.json"),
                Arguments.of("f1db-seasons-engine-manufacturers.sml", "f1db-seasons-engine-manufacturers.schema.json"),
                Arguments.of("f1db-seasons-tyre-manufacturers.sml", "f1db-seasons-tyre-manufacturers.schema.json"),
                Arguments.of("f1db-seasons-drivers.sml", "f1db-seasons-drivers.schema.json"),
                Arguments.of("f1db-seasons-driver-standings.sml", "f1db-seasons-driver-standings.schema.json"),
                Arguments.of("f1db-seasons-constructor-standings.sml", "f1db-seasons-constructor-standings.schema.json"),
                Arguments.of("f1db-races.sml", "f1db-races.schema.json"),
                Arguments.of("f1db-races-pre-qualifying-results.sml", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-free-practice-1-results.sml", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-free-practice-2-results.sml", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-free-practice-3-results.sml", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-free-practice-4-results.sml", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-qualifying-1-results.sml", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-qualifying-2-results.sml", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-qualifying-results.sml", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-sprint-qualifying-results.sml", "f1db-races-qualifying-results.schema.json"),
                Arguments.of("f1db-races-sprint-starting-grid-positions.sml", "f1db-races-starting-grid-positions.schema.json"),
                Arguments.of("f1db-races-sprint-race-results.sml", "f1db-races-race-results.schema.json"),
                Arguments.of("f1db-races-warming-up-results.sml", "f1db-races-practice-results.schema.json"),
                Arguments.of("f1db-races-starting-grid-positions.sml", "f1db-races-starting-grid-positions.schema.json"),
                Arguments.of("f1db-races-race-results.sml", "f1db-races-race-results.schema.json"),
                Arguments.of("f1db-races-fastest-laps.sml", "f1db-races-fastest-laps.schema.json"),
                Arguments.of("f1db-races-pit-stops.sml", "f1db-races-pit-stops.schema.json"),
                Arguments.of("f1db-races-driver-of-the-day-results.sml", "f1db-races-driver-of-the-day-results.schema.json"),
                Arguments.of("f1db-races-driver-standings.sml", "f1db-races-driver-standings.schema.json"),
                Arguments.of("f1db-races-constructor-standings.sml", "f1db-races-constructor-standings.schema.json"),
                Arguments.of("f1db-continents.sml", "f1db-continents.schema.json"),
                Arguments.of("f1db-countries.sml", "f1db-countries.schema.json"),
            )
        }
    }

    @Test
    @DisplayName("test single f1db.sml file")
    fun `test single f1db smile file`() {
        validate("f1db.sml", "single/f1db.schema.json")
    }

    @ParameterizedTest(name = "test splitted {0} file")
    @MethodSource("splittedFiles")
    fun `test splitted smile file`(smileFile: String, schemaFile: String) {
        validate(smileFile, "splitted/$schemaFile")
    }

    private fun validate(smileFile: String, schemaFile: String) {
        validate(File("../build/data/smile/$smileFile"), File("../src/schema/current/$schemaFile"))
    }

    private fun validate(smileFile: File, schemaFile: File) {
        val json = ByteArrayOutputStream()
        SmileFactory().createParser(smileFile).use { parser ->
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
