package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Season
import net.f1db.SeasonConstructorStanding

/**
 * The splitted season constructor standing.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "year",
        "positionDisplayOrder",
        "positionNumber",
        "positionText",
        "constructorId",
        "engineManufacturerId",
        "points"
])
class SplittedSeasonConstructorStanding {

    @JsonProperty("year")
    Integer year

    @JsonProperty("positionDisplayOrder")
    Integer positionDisplayOrder

    @Delegate
    private SeasonConstructorStanding constructorStanding

    SplittedSeasonConstructorStanding(Season season, Integer positionDisplayOrder, SeasonConstructorStanding constructorStanding) {
        this.year = season.year
        this.positionDisplayOrder = positionDisplayOrder
        this.constructorStanding = constructorStanding
    }
}
