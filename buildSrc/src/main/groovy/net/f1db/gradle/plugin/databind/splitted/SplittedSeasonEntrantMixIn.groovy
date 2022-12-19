package net.f1db.gradle.plugin.databind.splitted

import com.fasterxml.jackson.annotation.JsonIgnore
import net.f1db.SeasonEntrantConstructor

/**
 * The splitted season entrant mix-in.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedSeasonEntrantMixIn {

    @JsonIgnore
    List<SeasonEntrantConstructor> constructors

    @JsonIgnore
    abstract List<SeasonEntrantConstructor> getConstructors()
}
