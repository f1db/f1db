package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonTyreManufacturer
import com.f1db.plugin.writer.sql.tables.records.SeasonTyreManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonTyreManufacturerMapper : RecordUnmapper<SeasonTyreManufacturer, SeasonTyreManufacturerRecord> {

    override fun unmap(source: SeasonTyreManufacturer): SeasonTyreManufacturerRecord

    fun unmap(sources: List<SeasonTyreManufacturer>): List<SeasonTyreManufacturerRecord>
}
