package com.f1db.plugin.reader.databind

import com.f1db.plugin.schema.single.RaceConstructorStanding
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The race construtor standing converter.
 *
 * @author Marcel Overdijk
 */
class RaceConstructorStandingConverter : StdConverter<RaceConstructorStanding, RaceConstructorStanding>() {

    override fun convert(constructorStanding: RaceConstructorStanding): RaceConstructorStanding {
        constructorStanding.positionNumber = constructorStanding.positionText.toIntOrNull()
        return constructorStanding
    }
}
