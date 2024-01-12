package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.toMillis
import com.f1db.schema.single.PitStop
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The pit stop converter.
 *
 * @author Marcel Overdijk
 */
class PitStopConverter : StdConverter<PitStop, PitStop>() {

    override fun convert(pitStop: PitStop): PitStop {
        pitStop.positionNumber = pitStop.positionText.toIntOrNull()
        pitStop.timeMillis = pitStop.time.toMillis()
        return pitStop
    }
}
