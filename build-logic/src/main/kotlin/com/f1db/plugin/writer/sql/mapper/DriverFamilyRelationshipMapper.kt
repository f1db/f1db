package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.DriverFamilyRelationship
import com.f1db.plugin.writer.sql.tables.records.DriverFamilyRelationshipRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The driver family relationship mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface DriverFamilyRelationshipMapper : RecordUnmapper<DriverFamilyRelationship, DriverFamilyRelationshipRecord> {

    @Mapping(target = "driverId", source = "parentDriverId")
    @Mapping(target = "otherDriverId", source = "driverId")
    override fun unmap(source: DriverFamilyRelationship): DriverFamilyRelationshipRecord

    fun unmap(sources: List<DriverFamilyRelationship>): List<DriverFamilyRelationshipRecord>
}
