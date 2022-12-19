package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonProperty
import net.f1db.Race

/**
 * The splitted race data.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedRaceData {

    @JsonProperty("raceId")
    Integer raceId

    @JsonProperty("year")
    Integer year

    @JsonProperty("round")
    Integer round

    @JsonProperty("positionDisplayOrder")
    Integer positionDisplayOrder

    SplittedRaceData(Race race, Integer positionDisplayOrder) {
        this.raceId = race.id
        this.year = race.year
        this.round = race.round
        this.positionDisplayOrder = positionDisplayOrder
    }
}
