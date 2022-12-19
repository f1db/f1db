package net.f1db.gradle.plugin.dto.splitted

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.f1db.Driver
import net.f1db.DriverFamilyRelationship

/**
 * The splitted driver family relationship.
 *
 * @author Marcel Overdijk
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder([
        "parentDriverId",
        "driverId",
        "type"
])
class SplittedDriverFamilyRelationship {

    @JsonProperty("parentDriverId")
    String parentDriverId

    @Delegate
    private DriverFamilyRelationship familyRelationship

    SplittedDriverFamilyRelationship(Driver driver, DriverFamilyRelationship familyRelationship) {
        this.parentDriverId = driver.id
        this.familyRelationship = familyRelationship
    }
}
