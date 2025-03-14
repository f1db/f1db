package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.toRounds
import com.f1db.plugin.schema.single.SeasonEntrantDriver
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The season entrant driver converter.
 *
 * @author Marcel Overdijk
 */
class SeasonEntrantDriverConverter : StdConverter<SeasonEntrantDriver, SeasonEntrantDriver>() {

    override fun convert(seasonEntrantDriver: SeasonEntrantDriver): SeasonEntrantDriver {
        seasonEntrantDriver.rounds = seasonEntrantDriver.roundsText.toRounds()
        return seasonEntrantDriver
    }
}
