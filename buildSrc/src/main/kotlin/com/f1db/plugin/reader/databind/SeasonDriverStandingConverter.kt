package com.f1db.plugin.reader.databind

import com.f1db.schema.single.SeasonDriverStanding
import com.fasterxml.jackson.databind.util.StdConverter

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
