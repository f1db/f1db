package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Season
import net.f1db.SeasonDriverStanding

/**
 * The splitted season driver standing.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "year",
        "positionDisplayOrder",
        "positionNumber",
        "positionText",
        "driverId",
        "points"
])
class SplittedSeasonDriverStanding {

    @JsonProperty("year")
    Integer year

    @JsonProperty("positionDisplayOrder")
    Integer positionDisplayOrder

    @Delegate
    private SeasonDriverStanding driverStanding

    SplittedSeasonDriverStanding(Season season, Integer positionDisplayOrder, SeasonDriverStanding driverStanding) {
        this.year = season.year
        this.positionDisplayOrder = positionDisplayOrder
        this.driverStanding = driverStanding
    }
}
