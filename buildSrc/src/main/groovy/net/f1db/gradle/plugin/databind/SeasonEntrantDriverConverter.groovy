package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.SeasonEntrantDriver

/**
 * The season entrant driver converter.
 *
 * @author Marcel Overdijk
 */
class SeasonEntrantDriverConverter extends StdConverter<SeasonEntrantDriver, SeasonEntrantDriver> {

    @Override
    SeasonEntrantDriver convert(SeasonEntrantDriver seasonEntrantDriver) {
        seasonEntrantDriver.rounds = toRounds(seasonEntrantDriver.roundsText)
        return seasonEntrantDriver
    }

    static List<Integer> toRounds(String roundsText) {
        def rounds = []
        def roundsTextSplit = roundsText?.split(",")
        roundsTextSplit.each { round ->
            if (round.contains("-")) {
                def range = round.split("-")
                def start = range[0].toInteger()
                def end = range[1].toInteger()
                for (i in start..end) {
                    rounds << i
                }
            } else {
                rounds << round.toInteger()
            }
        }
        return rounds
    }
}
