package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The driver standing mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = DriverStandingConverter)
abstract class DriverStandingMixIn {

    @JsonIgnore
    Integer positionNumber

    @JsonProperty(value = "position")
    String positionText
}
