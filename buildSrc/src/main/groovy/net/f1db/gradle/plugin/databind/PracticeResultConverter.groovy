package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.PracticeResult

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull
import static net.f1db.gradle.plugin.F1DBReader.toMillis

/**
 * The practice result converter.
 *
 * @author Marcel Overdijk
 */
class PracticeResultConverter extends StdConverter<PracticeResult, PracticeResult> {

    @Override
    PracticeResult convert(PracticeResult practiceResult) {
        practiceResult.positionNumber = toIntegerOrNull practiceResult.positionText
        practiceResult.timeMillis = toMillis practiceResult.time
        practiceResult.gapMillis = toMillis practiceResult.gap
        practiceResult.intervalMillis = toMillis practiceResult.interval
        return practiceResult
    }
}
