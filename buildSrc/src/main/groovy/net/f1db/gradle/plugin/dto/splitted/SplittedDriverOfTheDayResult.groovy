package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.DriverOfTheDayResult
import net.f1db.Race

/**
 * The splitted driver of the day result.
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
        "percentage"
])
class SplittedDriverOfTheDayResult extends SplittedRaceData {

    @Delegate
    private DriverOfTheDayResult driverOfTheDayResult

    SplittedDriverOfTheDayResult(Race race, Integer positionDisplayOrder, DriverOfTheDayResult driverOfTheDayResult) {
        super(race, positionDisplayOrder)
        this.driverOfTheDayResult = driverOfTheDayResult
    }
}
