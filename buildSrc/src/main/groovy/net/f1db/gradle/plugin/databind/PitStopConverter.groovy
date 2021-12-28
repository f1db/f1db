package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.PitStop

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull
import static net.f1db.gradle.plugin.F1DBReader.toMillis

/**
 * The pit stop converter.
 *
 * @author Marcel Overdijk
 */
class PitStopConverter extends StdConverter<PitStop, PitStop> {

    @Override
    PitStop convert(PitStop pitStop) {
        pitStop.positionNumber = toIntegerOrNull pitStop.positionText
        pitStop.timeMillis = toMillis pitStop.time
        return pitStop
    }
}
