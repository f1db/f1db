package com.f1db.plugin.reader.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The driver of the day result mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = DriverOfTheDayResultConverter::class)
abstract class DriverOfTheDayResultMixIn {

    @JsonIgnore
    var positionNumber: Int = 1

    @JsonProperty(value = "position")
    var positionText: String = "1"
}
