package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.RaceResult

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull
import static net.f1db.gradle.plugin.F1DBReader.toMillis

/**
 * The race result converter.
 *
 * @author Marcel Overdijk
 */
class RaceResultConverter extends StdConverter<RaceResult, RaceResult> {

    @Override
    RaceResult convert(RaceResult raceResult) {
        raceResult.positionNumber = toIntegerOrNull raceResult.positionText
        raceResult.timeMillis = toMillis raceResult.time
        raceResult.timePenaltyMillis = toMillis raceResult.timePenalty
        if (raceResult.gap) {
            def lapsMatcher = raceResult.gap =~ /^\+(\d+) lap(s)?$/
            if (lapsMatcher.matches()) {
                raceResult.gapLaps = lapsMatcher[0][1] as Integer
            } else {
                raceResult.gapMillis = toMillis raceResult.gap
            }
        }
        raceResult.intervalMillis = toMillis raceResult.interval
        raceResult.gridPositionNumber = toIntegerOrNull raceResult.gridPositionText
        return raceResult
    }
}
