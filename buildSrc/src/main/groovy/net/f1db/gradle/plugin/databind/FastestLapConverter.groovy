package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.FastestLap

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull
import static net.f1db.gradle.plugin.F1DBReader.toMillis

/**
 * The fastest lap converter.
 *
 * @author Marcel Overdijk
 */
class FastestLapConverter extends StdConverter<FastestLap, FastestLap> {

    @Override
    FastestLap convert(FastestLap fastestLap) {
        fastestLap.positionNumber = toIntegerOrNull fastestLap.positionText
        fastestLap.timeMillis = toMillis fastestLap.time
        fastestLap.gapMillis = toMillis fastestLap.gap
        fastestLap.intervalMillis = toMillis fastestLap.interval
        return fastestLap
    }
}
