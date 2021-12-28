package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The starting grid position mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = StartingGridPositionConverter)
abstract class StartingGridPositionMixIn {

    @JsonIgnore
    Integer positionNumber

    @JsonProperty(value = "position")
    String positionText
}
