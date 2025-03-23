package com.f1db.plugin.reader.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The race driver standing mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = RaceDriverStandingConverter::class)
abstract class RaceDriverStandingMixIn {

    @JsonIgnore
    var positionNumber: Int = 1

    @JsonProperty(value = "position")
    var positionText: String = "1"
}
