package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.FastestLap
import net.f1db.Race

/**
 * The splitted fastest lap.
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
        "lap",
        "time",
        "timeMillis",
        "gap",
        "gapMillis",
        "interval",
        "intervalMillis"
])
class SplittedFastestLap extends SplittedRaceData {

    @Delegate
    private FastestLap fastestLap

    SplittedFastestLap(Race race, Integer positionDisplayOrder, FastestLap fastestLap) {
        super(race, positionDisplayOrder)
        this.fastestLap = fastestLap
    }
}
