package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toMillis
import com.onlyf1.db.schema.single.PracticeResult

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
