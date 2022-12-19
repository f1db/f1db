package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Race
import net.f1db.RaceResult

/**
 * The splitted race result.
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
        "sharedCar",
        "laps",
        "time",
        "timeMillis",
        "timePenalty",
        "timePenaltyMillis",
        "gap",
        "gapMillis",
        "gapLaps",
        "interval",
        "intervalMillis",
        "reasonRetired",
        "points",
        "gridPositionNumber",
        "gridPositionText",
        "positionsGained",
        "fastestLap",
        "pitStops",
        "driverOfTheDay"
])
class SplittedRaceResult extends SplittedRaceData {

    @Delegate
    private RaceResult raceResult

    SplittedRaceResult(Race race, Integer positionDisplayOrder, RaceResult raceResult) {
        super(race, positionDisplayOrder)
        this.raceResult = raceResult
    }
}
