package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Race
import net.f1db.RaceDriverStanding

/**
 * The splitted race driver standing.
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
        "driverId",
        "points",
        "positionsGained"
])
class SplittedRaceDriverStanding {

    @JsonProperty("raceId")
    Integer raceId

    @JsonProperty("year")
    Integer year

    @JsonProperty("round")
    Integer round

    @JsonProperty("positionDisplayOrder")
    Integer positionDisplayOrder

    @Delegate
    private RaceDriverStanding driverStanding

    SplittedRaceDriverStanding(Race race, Integer positionDisplayOrder, RaceDriverStanding driverStanding) {
        this.raceId = race.id
        this.year = race.year
        this.round = race.round
        this.positionDisplayOrder = positionDisplayOrder
        this.driverStanding = driverStanding
    }
}
