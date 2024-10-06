package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Chassis
import com.f1db.plugin.writer.sql.tables.records.ChassisRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The chassis mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ChassisMapper : RecordUnmapper<Chassis, ChassisRecord> {

    override fun unmap(source: Chassis): ChassisRecord

    fun unmap(sources: List<Chassis>): List<ChassisRecord>
}
