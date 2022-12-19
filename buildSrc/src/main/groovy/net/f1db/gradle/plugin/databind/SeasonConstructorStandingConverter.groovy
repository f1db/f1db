package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.SeasonConstructorStanding

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The season constructor standing converter.
 *
 * @author Marcel Overdijk
 */
class SeasonConstructorStandingConverter extends StdConverter<SeasonConstructorStanding, SeasonConstructorStanding> {

    @Override
    SeasonConstructorStanding convert(SeasonConstructorStanding constructorStanding) {
        constructorStanding.positionNumber = toIntegerOrNull constructorStanding.positionText
        return constructorStanding
    }
}
