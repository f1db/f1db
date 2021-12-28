package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The season entrant driver mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = SeasonEntrantDriverConverter)
abstract class SeasonEntrantDriverMixIn {

    @JsonIgnore
    Set<Integer> rounds

    @JsonProperty(value = "rounds")
    abstract void setRoundsText(String roundsText)
}
