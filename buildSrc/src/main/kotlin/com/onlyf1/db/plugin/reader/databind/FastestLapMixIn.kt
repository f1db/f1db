package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

/**
 * The fastest lap mix-in.
 *
 * @author Marcel Overdijk
 */
@JsonDeserialize(converter = FastestLapConverter::class)
abstract class FastestLapMixIn {

    @JsonIgnore
    var positionNumber: Int = 1

    @JsonProperty(value = "position")
    var positionText: String = "1"
}
