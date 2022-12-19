package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.RaceConstructorStanding

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The race constructor standing converter.
 *
 * @author Marcel Overdijk
 */
class RaceConstructorStandingConverter extends StdConverter<RaceConstructorStanding, RaceConstructorStanding> {

    @Override
    RaceConstructorStanding convert(RaceConstructorStanding constructorStanding) {
        constructorStanding.positionNumber = toIntegerOrNull constructorStanding.positionText
        return constructorStanding
    }
}
