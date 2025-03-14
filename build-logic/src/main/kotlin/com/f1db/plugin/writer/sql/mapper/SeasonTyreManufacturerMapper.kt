package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonTyreManufacturer
import com.f1db.plugin.writer.sql.tables.records.SeasonTyreManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The season tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonTyreManufacturerMapper : RecordUnmapper<SeasonTyreManufacturer, SeasonTyreManufacturerRecord> {

    override fun unmap(source: SeasonTyreManufacturer): SeasonTyreManufacturerRecord

    fun unmap(sources: List<SeasonTyreManufacturer>): List<SeasonTyreManufacturerRecord>
}
