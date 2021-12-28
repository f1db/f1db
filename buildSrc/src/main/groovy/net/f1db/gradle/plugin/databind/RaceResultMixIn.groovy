package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The race result mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = RaceResultConverter)
abstract class RaceResultMixIn {

    @JsonIgnore
    Integer positionNumber

    @JsonProperty(value = "position")
    String positionText

    @JsonIgnore
    Integer gridPositionNumber

    @JsonProperty(value = "gridPosition")
    String gridPositionText
}
