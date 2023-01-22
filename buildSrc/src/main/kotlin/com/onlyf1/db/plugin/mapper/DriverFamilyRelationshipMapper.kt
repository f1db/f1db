package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Driver
import com.onlyf1.db.schema.single.DriverFamilyRelationship
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.DriverFamilyRelationship as SplittedDriverFamilyRelationship

/**
 * The driver family relationship mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface DriverFamilyRelationshipMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(DriverFamilyRelationshipMapper::class.java)
    }

    @Mapping(target = "parentDriverId", ignore = true)
    fun toSplittedDriverFamilyRelationship(driverFamilyRelationship: DriverFamilyRelationship, @Context parentDriver: Driver): SplittedDriverFamilyRelationship

    fun toSplittedDriverFamilyRelationships(
        driverFamilyRelationships: List<DriverFamilyRelationship>,
        @Context parentDriver: Driver
    ): List<SplittedDriverFamilyRelationship>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedDriverFamilyRelationship: SplittedDriverFamilyRelationship, @Context parentDriver: Driver) {
        splittedDriverFamilyRelationship.parentDriverId = parentDriver.id
    }
}
