package com.f1db.plugin.reader.databind

import com.f1db.plugin.schema.single.SeasonConstructorStanding
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The season construtor standing converter.
 *
 * @author Marcel Overdijk
 */
class SeasonConstructorStandingConverter : StdConverter<SeasonConstructorStanding, SeasonConstructorStanding>() {

    override fun convert(constructorStanding: SeasonConstructorStanding): SeasonConstructorStanding {
        constructorStanding.positionNumber = constructorStanding.positionText.toIntOrNull()
        return constructorStanding
    }
}
