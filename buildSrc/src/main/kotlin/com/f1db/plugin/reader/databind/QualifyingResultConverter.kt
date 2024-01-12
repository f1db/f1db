package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.toMillis
import com.f1db.schema.single.QualifyingResult
import com.fasterxml.jackson.databind.util.StdConverter

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
