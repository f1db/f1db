package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.schema.single.SeasonDriverStanding

/**
 * The season driver standing converter.
 *
 * @author Marcel Overdijk
 */
class SeasonDriverStandingConverter : StdConverter<SeasonDriverStanding, SeasonDriverStanding>() {

    override fun convert(driverStanding: SeasonDriverStanding): SeasonDriverStanding {
        driverStanding.positionNumber = driverStanding.positionText.toIntOrNull()
        return driverStanding
    }
}
