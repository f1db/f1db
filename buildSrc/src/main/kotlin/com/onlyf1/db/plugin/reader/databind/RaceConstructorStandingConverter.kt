package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.schema.single.RaceConstructorStanding

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
