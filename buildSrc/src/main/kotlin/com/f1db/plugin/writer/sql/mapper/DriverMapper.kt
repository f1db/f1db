package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Driver
import com.f1db.plugin.writer.sql.tables.records.DriverRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface DriverMapper : RecordUnmapper<Driver, DriverRecord> {

    override fun unmap(source: Driver): DriverRecord

    fun unmap(sources: List<Driver>): List<DriverRecord>
}
