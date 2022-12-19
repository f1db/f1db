package net.f1db.gradle.plugin.databind.splitted

import com.fasterxml.jackson.annotation.JsonIgnore
import net.f1db.SeasonConstructorStanding
import net.f1db.SeasonDriverStanding
import net.f1db.SeasonEntrant

/**
 * The splitted season mix-in.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedSeasonMixIn {

    @JsonIgnore
    List<SeasonEntrant> entrants

    @JsonIgnore
    List<SeasonDriverStanding> driverStandings

    @JsonIgnore
    List<SeasonConstructorStanding> constructorStandings

    @JsonIgnore
    abstract List<SeasonEntrant> getEntrants()

    @JsonIgnore
    abstract List<SeasonDriverStanding> getDriverStandings()

    @JsonIgnore
    abstract List<SeasonConstructorStanding> getConstructorStandings()
}
