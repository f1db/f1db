package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.PracticeResult
import net.f1db.Race

/**
 * The splitted practice result.
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
        "gap",
        "gapMillis",
        "interval",
        "intervalMillis",
        "laps"
])
class SplittedPracticeResult extends SplittedRaceData {

    @Delegate
    private PracticeResult practiceResult

    SplittedPracticeResult(Race race, Integer positionDisplayOrder, PracticeResult practiceResult) {
        super(race, positionDisplayOrder)
        this.practiceResult = practiceResult
    }
}
