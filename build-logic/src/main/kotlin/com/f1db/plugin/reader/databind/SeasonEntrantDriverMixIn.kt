package com.f1db.plugin.reader.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The season entrant driver mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = SeasonEntrantDriverConverter::class)
abstract class SeasonEntrantDriverMixIn {

    @JsonIgnore
    var rounds: Set<Int> = mutableSetOf()

    @JsonProperty(value = "rounds")
    abstract fun setRoundsText(roundsText: String)
}
