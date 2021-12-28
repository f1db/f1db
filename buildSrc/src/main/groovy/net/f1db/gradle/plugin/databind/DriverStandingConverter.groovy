package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.DriverStanding

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The driver standing converter.
 *
 * @author Marcel Overdijk
 */
class DriverStandingConverter extends StdConverter<DriverStanding, DriverStanding> {

    @Override
    DriverStanding convert(DriverStanding driverStanding) {
        driverStanding.positionNumber = toIntegerOrNull driverStanding.positionText
        return driverStanding
    }
}
