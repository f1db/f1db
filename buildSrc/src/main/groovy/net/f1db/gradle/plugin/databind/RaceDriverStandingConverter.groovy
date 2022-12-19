package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.RaceDriverStanding

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The race driver standing converter.
 *
 * @author Marcel Overdijk
 */
class RaceDriverStandingConverter extends StdConverter<RaceDriverStanding, RaceDriverStanding> {

    @Override
    RaceDriverStanding convert(RaceDriverStanding driverStanding) {
        driverStanding.positionNumber = toIntegerOrNull driverStanding.positionText
        return driverStanding
    }
}
