package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The season constructor standing mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = SeasonConstructorStandingConverter)
abstract class SeasonConstructorStandingMixIn {

    @JsonIgnore
    Integer positionNumber

    @JsonProperty(value = "position")
    String positionText
}
