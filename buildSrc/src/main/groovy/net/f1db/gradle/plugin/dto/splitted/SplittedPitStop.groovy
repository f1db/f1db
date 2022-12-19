package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.PitStop
import net.f1db.Race

/**
 * The splitted pit stop.
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
        "stop",
        "lap",
        "time",
        "timeMillis"
])
class SplittedPitStop extends SplittedRaceData {

    @Delegate
    private PitStop pitStop

    SplittedPitStop(Race race, Integer positionDisplayOrder, PitStop pitStop) {
        super(race, positionDisplayOrder)
        this.pitStop = pitStop
    }
}
