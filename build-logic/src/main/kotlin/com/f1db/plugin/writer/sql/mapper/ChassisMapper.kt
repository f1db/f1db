package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Chassis
import com.f1db.plugin.writer.sql.tables.records.ChassisRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The chassis mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface ChassisMapper : RecordUnmapper<Chassis, ChassisRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    @Mapping(target = "fullNameNormalized", source = "fullName", qualifiedByName = ["normalize"])
    override fun unmap(source: Chassis): ChassisRecord

    fun unmap(sources: List<Chassis>): List<ChassisRecord>
}
