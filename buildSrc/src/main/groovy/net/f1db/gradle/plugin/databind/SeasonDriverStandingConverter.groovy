package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.SeasonDriverStanding

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The season driver standing converter.
 *
 * @author Marcel Overdijk
 */
class SeasonDriverStandingConverter extends StdConverter<SeasonDriverStanding, SeasonDriverStanding> {

    @Override
    SeasonDriverStanding convert(SeasonDriverStanding driverStanding) {
        driverStanding.positionNumber = toIntegerOrNull driverStanding.positionText
        return driverStanding
    }
}
