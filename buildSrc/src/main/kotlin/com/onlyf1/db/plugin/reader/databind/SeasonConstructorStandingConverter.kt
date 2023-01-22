package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.schema.single.SeasonConstructorStanding

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
