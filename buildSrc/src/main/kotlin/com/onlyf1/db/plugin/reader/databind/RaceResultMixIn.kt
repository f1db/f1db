package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The race result mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = RaceResultConverter::class)
abstract class RaceResultMixIn {

    @JsonIgnore
    var positionNumber: Int = 1

    @JsonProperty(value = "position")
    var positionText: String = "1"

    @JsonIgnore
    var gridPositionNumber: Int = 1

    @JsonProperty(value = "gridPosition")
    var gridPositionText: String = "1"
}
