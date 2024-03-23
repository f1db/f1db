package com.f1db.plugin.reader.databind

import com.f1db.plugin.schema.single.RaceDriverStanding
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The race driver standing converter.
 *
 * @author Marcel Overdijk
 */
class RaceDriverStandingConverter : StdConverter<RaceDriverStanding, RaceDriverStanding>() {

    override fun convert(driverStanding: RaceDriverStanding): RaceDriverStanding {
        driverStanding.positionNumber = driverStanding.positionText.toIntOrNull()
        return driverStanding
    }
}
