package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.QualifyingResult

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull
import static net.f1db.gradle.plugin.F1DBReader.toMillis

/**
 * The qualifying result converter.
 *
 * @author Marcel Overdijk
 */
class QualifyingResultConverter extends StdConverter<QualifyingResult, QualifyingResult> {

    @Override
    QualifyingResult convert(QualifyingResult qualifyingResult) {
        qualifyingResult.positionNumber = toIntegerOrNull qualifyingResult.positionText
        qualifyingResult.timeMillis = toMillis qualifyingResult.time
        qualifyingResult.q1Millis = toMillis qualifyingResult.q1
        qualifyingResult.q2Millis = toMillis qualifyingResult.q2
        qualifyingResult.q3Millis = toMillis qualifyingResult.q3
        qualifyingResult.gapMillis = toMillis qualifyingResult.gap
        qualifyingResult.intervalMillis = toMillis qualifyingResult.interval
        return qualifyingResult
    }
}
