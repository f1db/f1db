package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Season
import net.f1db.SeasonEntrant
import net.f1db.SeasonEntrantConstructor

/**
 * The splitted season entrant constructor.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "year",
        "entrantId",
        "constructorId",
        "engineManufacturerId"
])
class SplittedSeasonEntrantConstructor {

    @JsonProperty("year")
    Integer year

    @JsonProperty("entrantId")
    String entrantId

    @Delegate
    private SeasonEntrantConstructor seasonEntrantConstructor

    SplittedSeasonEntrantConstructor(Season season, SeasonEntrant seasonEntrant, SeasonEntrantConstructor seasonEntrantConstructor) {
        this.year = season.year
        this.entrantId = seasonEntrant.entrantId
        this.seasonEntrantConstructor = seasonEntrantConstructor
    }
}
