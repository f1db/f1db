package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Season
import net.f1db.SeasonEntrant
import net.f1db.SeasonEntrantConstructor
import net.f1db.SeasonEntrantDriver

/**
 * The splitted season entrant driver.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "year",
        "entrantId",
        "constructorId",
        "engineManufacturerId",
        "driverId",
        "rounds",
        "roundsText",
        "testDriver"
])
class SplittedSeasonEntrantDriver {

    @JsonProperty("year")
    Integer year

    @JsonProperty("entrantId")
    String entrantId

    @JsonProperty("constructorId")
    String constructorId

    @JsonProperty("engineManufacturerId")
    String engineManufacturerId

    @Delegate
    private SeasonEntrantDriver seasonEntrantDriver

    SplittedSeasonEntrantDriver(Season season, SeasonEntrant seasonEntrant, SeasonEntrantConstructor seasonEntrantConstructor, SeasonEntrantDriver seasonEntrantDriver) {
        this.year = season.year
        this.entrantId = seasonEntrant.entrantId
        this.constructorId = seasonEntrantConstructor.constructorId
        this.engineManufacturerId = seasonEntrantConstructor.engineManufacturerId
        this.seasonEntrantDriver = seasonEntrantDriver
    }
}
