package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.EngineManufacturer
import com.f1db.plugin.writer.sql.tables.records.EngineManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The engine manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface EngineManufacturerMapper : RecordUnmapper<EngineManufacturer, EngineManufacturerRecord> {

    override fun unmap(source: EngineManufacturer): EngineManufacturerRecord

    fun unmap(sources: List<EngineManufacturer>): List<EngineManufacturerRecord>
}
