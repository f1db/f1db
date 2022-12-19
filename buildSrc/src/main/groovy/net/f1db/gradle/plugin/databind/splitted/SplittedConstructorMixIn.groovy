package net.f1db.gradle.plugin.databind.splitted

import com.fasterxml.jackson.annotation.JsonIgnore
import net.f1db.PreviousNextConstructor

/**
 * The splitted constructor mix-in.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedConstructorMixIn {

    @JsonIgnore
    List<PreviousNextConstructor> previousNextConstructors

    @JsonIgnore
    abstract List<PreviousNextConstructor> getPreviousNextConstructors()
}
