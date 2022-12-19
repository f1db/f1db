package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Race
import net.f1db.StartingGridPosition

/**
 * The splitted starting grid position.
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
        "gridPenalty",
        "gridPenaltyPositions",
        "time",
        "timeMillis"
])
class SplittedStartingGridPosition extends SplittedRaceData {

    @Delegate
    private StartingGridPosition startingGridPosition

    SplittedStartingGridPosition(Race race, Integer positionDisplayOrder, StartingGridPosition startingGridPosition) {
        super(race, positionDisplayOrder)
        this.startingGridPosition = startingGridPosition
    }
}
