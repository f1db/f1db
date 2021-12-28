package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The driver of the day result mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = DriverOfTheDayResultConverter)
abstract class DriverOfTheDayResultMixIn {

    @JsonIgnore
    Integer positionNumber

    @JsonProperty(value = "position")
    String positionText
}
