package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toMillis
import com.onlyf1.db.schema.single.FastestLap

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
