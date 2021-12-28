package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.ConstructorStanding

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull

/**
 * The constructor standing converter.
 *
 * @author Marcel Overdijk
 */
class ConstructorStandingConverter extends StdConverter<ConstructorStanding, ConstructorStanding> {

    @Override
    ConstructorStanding convert(ConstructorStanding constructorStanding) {
        constructorStanding.positionNumber = toIntegerOrNull constructorStanding.positionText
        return constructorStanding
    }
}
