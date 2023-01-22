package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toMillis
import com.onlyf1.db.schema.single.PitStop

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
