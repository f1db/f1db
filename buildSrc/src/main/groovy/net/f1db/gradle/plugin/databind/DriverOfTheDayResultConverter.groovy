package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.DriverOfTheDayResult

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The driver of the day result converter.
 *
 * @author Marcel Overdijk
 */
class DriverOfTheDayResultConverter extends StdConverter<DriverOfTheDayResult, DriverOfTheDayResult> {

    @Override
    DriverOfTheDayResult convert(DriverOfTheDayResult driverOfTheDayResult) {
        driverOfTheDayResult.positionNumber = toIntegerOrNull driverOfTheDayResult.positionText
        return driverOfTheDayResult
    }
}
