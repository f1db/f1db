package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.toMillis
import com.f1db.schema.single.FastestLap
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The fastest lap converter.
 *
 * @author Marcel Overdijk
 */
class FastestLapConverter : StdConverter<FastestLap, FastestLap>() {

    override fun convert(fastestLap: FastestLap): FastestLap {
        fastestLap.positionNumber = fastestLap.positionText.toIntOrNull()
        fastestLap.timeMillis = fastestLap.time.toMillis()
        fastestLap.gapMillis = fastestLap.gap.toMillis()
        fastestLap.intervalMillis = fastestLap.interval.toMillis()
        return fastestLap
    }
}
