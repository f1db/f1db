package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The pit stop mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = PitStopConverter)
abstract class PitStopMixIn {

    @JsonIgnore
    Integer positionNumber

    @JsonProperty(value = "position")
    String positionText
}
