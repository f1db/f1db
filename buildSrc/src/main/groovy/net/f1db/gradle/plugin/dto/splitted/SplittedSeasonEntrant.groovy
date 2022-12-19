package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Season
import net.f1db.SeasonEntrant

/**
 * The splitted season entrant.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "year",
        "entrantId",
        "countryId"
])
class SplittedSeasonEntrant {

    @JsonProperty("year")
    Integer year

    @Delegate
    private SeasonEntrant seasonEntrant

    SplittedSeasonEntrant(Season season, SeasonEntrant seasonEntrant) {
        this.year = season.year
        this.seasonEntrant = seasonEntrant
    }
}
