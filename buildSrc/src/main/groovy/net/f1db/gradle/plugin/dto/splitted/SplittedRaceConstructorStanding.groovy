package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Race
import net.f1db.RaceConstructorStanding

/**
 * The splitted race constructor standing.
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
        "constructorId",
        "engineManufacturerId",
        "points",
        "positionsGained"
])
class SplittedRaceConstructorStanding {

    @JsonProperty("raceId")
    Integer raceId

    @JsonProperty("year")
    Integer year

    @JsonProperty("round")
    Integer round

    @JsonProperty("positionDisplayOrder")
    Integer positionDisplayOrder

    @Delegate
    private RaceConstructorStanding constructorStanding

    SplittedRaceConstructorStanding(Race race, Integer positionDisplayOrder, RaceConstructorStanding constructorStanding) {
        this.raceId = race.id
        this.year = race.year
        this.round = race.round
        this.positionDisplayOrder = positionDisplayOrder
        this.constructorStanding = constructorStanding
    }
}
