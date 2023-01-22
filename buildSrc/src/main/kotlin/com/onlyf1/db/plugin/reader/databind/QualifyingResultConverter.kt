package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toMillis
import com.onlyf1.db.schema.single.QualifyingResult

/**
 * The qualifying result converter.
 *
 * @author Marcel Overdijk
 */
class QualifyingResultConverter : StdConverter<QualifyingResult, QualifyingResult>() {

    override fun convert(qualifyingResult: QualifyingResult): QualifyingResult {
        qualifyingResult.positionNumber = qualifyingResult.positionText.toIntOrNull()
        qualifyingResult.timeMillis = qualifyingResult.time.toMillis()
        qualifyingResult.q1Millis = qualifyingResult.q1.toMillis()
        qualifyingResult.q2Millis = qualifyingResult.q2.toMillis()
        qualifyingResult.q3Millis = qualifyingResult.q3.toMillis()
        qualifyingResult.gapMillis = qualifyingResult.gap.toMillis()
        qualifyingResult.intervalMillis = qualifyingResult.interval.toMillis()
        return qualifyingResult
    }
}
