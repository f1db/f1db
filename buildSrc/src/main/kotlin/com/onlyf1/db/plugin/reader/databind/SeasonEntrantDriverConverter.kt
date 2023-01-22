package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toRounds
import com.onlyf1.db.schema.single.SeasonEntrantDriver

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
