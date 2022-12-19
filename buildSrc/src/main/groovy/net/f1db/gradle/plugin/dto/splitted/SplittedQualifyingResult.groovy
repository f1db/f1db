package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.QualifyingResult
import net.f1db.Race

/**
 * The splitted qualifying result.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "raceId",
        "year",
        "round",
        "positionDisplayOrder",
        "positionNumber",
        "positionText",
        "driverNumber",
        "driverId",
        "constructorId",
        "engineManufacturerId",
        "tyreManufacturerId",
        "time",
        "timeMillis",
        "q1",
        "q1Millis",
        "q2",
        "q2Millis",
        "q3",
        "q3Millis",
        "gap",
        "gapMillis",
        "interval",
        "intervalMillis",
        "laps"
])
class SplittedQualifyingResult extends SplittedRaceData {

    @Delegate
    private QualifyingResult qualifyingResult

    SplittedQualifyingResult(Race race, Integer positionDisplayOrder, QualifyingResult qualifyingResult) {
        super(race, positionDisplayOrder)
        this.qualifyingResult = qualifyingResult
    }
}
