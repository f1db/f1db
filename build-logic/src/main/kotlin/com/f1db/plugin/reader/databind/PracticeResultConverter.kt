package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.toMillis
import com.f1db.plugin.schema.single.PracticeResult
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The practice result converter.
 *
 * @author Marcel Overdijk
 */
class PracticeResultConverter : StdConverter<PracticeResult, PracticeResult>() {

    override fun convert(practiceResult: PracticeResult): PracticeResult {
        practiceResult.positionNumber = practiceResult.positionText.toIntOrNull()
        practiceResult.timeMillis = practiceResult.time.toMillis()
        practiceResult.gapMillis = practiceResult.gap.toMillis()
        practiceResult.intervalMillis = practiceResult.interval.toMillis()
        return practiceResult
    }
}
