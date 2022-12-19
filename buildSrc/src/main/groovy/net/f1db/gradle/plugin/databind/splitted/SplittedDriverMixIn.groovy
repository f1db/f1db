package net.f1db.gradle.plugin.databind.splitted

import com.fasterxml.jackson.annotation.JsonIgnore
import net.f1db.DriverFamilyRelationship

/**
 * The splitted driver mix-in.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedDriverMixIn {

    @JsonIgnore
    List<DriverFamilyRelationship> familyRelationships

    @JsonIgnore
    abstract List<DriverFamilyRelationship> getFamilyRelationships()
}
