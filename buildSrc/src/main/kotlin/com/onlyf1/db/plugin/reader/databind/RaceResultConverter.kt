package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toMillis
import com.onlyf1.db.schema.single.RaceResult

/**
 * The race result converter.
 *
 * @author Marcel Overdijk
 */
class RaceResultConverter : StdConverter<RaceResult, RaceResult>() {

    override fun convert(raceResult: RaceResult): RaceResult {
        raceResult.positionNumber = raceResult.positionText.toIntOrNull()
        raceResult.timeMillis = raceResult.time.toMillis()
        raceResult.timePenaltyMillis = raceResult.timePenalty.toMillis()
        if (raceResult.gap != null) {
            val lapsRegex = """^\+(\d+) lap(s)?${'$'}""".toRegex()
            val lapsMatchResult = lapsRegex.matchEntire(raceResult.gap)
            if (lapsMatchResult != null) {
                raceResult.gapLaps = lapsMatchResult.groupValues[1].toInt()
            } else {
                raceResult.gapMillis = raceResult.gap.toMillis()
            }
        }
        raceResult.intervalMillis = raceResult.interval.toMillis()
        raceResult.gridPositionNumber = raceResult.gridPositionText?.toIntOrNull()
        return raceResult
    }
}
