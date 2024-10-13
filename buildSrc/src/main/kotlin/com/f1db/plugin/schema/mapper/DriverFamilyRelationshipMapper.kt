package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Driver
import com.f1db.plugin.schema.single.DriverFamilyRelationship
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.DriverFamilyRelationship as SplittedDriverFamilyRelationship

/**
 * The driver family relationship mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface DriverFamilyRelationshipMapper {

    @Mapping(target = "parentDriverId", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedDriverFamilyRelationship(driverFamilyRelationship: DriverFamilyRelationship, @Context parentDriver: Driver, @Context index: Int): SplittedDriverFamilyRelationship

    @AfterMapping
    fun afterMapping(@MappingTarget splittedDriverFamilyRelationship: SplittedDriverFamilyRelationship, @Context parentDriver: Driver, @Context index: Int) {
        splittedDriverFamilyRelationship.parentDriverId = parentDriver.id
        splittedDriverFamilyRelationship.positionDisplayOrder = index;
    }
}
