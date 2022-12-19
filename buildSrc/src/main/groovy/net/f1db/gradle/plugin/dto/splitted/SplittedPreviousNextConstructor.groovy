package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Constructor
import net.f1db.PreviousNextConstructor

/**
 * The splitted previous/next constructor.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "parentConstructorId",
        "constructorId",
        "yearFrom",
        "yearTo"
])
class SplittedPreviousNextConstructor {

    @JsonProperty("parentConstructorId")
    String parentConstructorId

    @Delegate
    private PreviousNextConstructor previousNextConstructor

    SplittedPreviousNextConstructor(Constructor constructor, PreviousNextConstructor previousNextConstructor) {
        this.parentConstructorId = constructor.id
        this.previousNextConstructor = previousNextConstructor
    }
}
