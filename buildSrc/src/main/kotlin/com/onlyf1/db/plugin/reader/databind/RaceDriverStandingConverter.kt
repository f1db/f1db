package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.schema.single.RaceDriverStanding

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
