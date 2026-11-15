package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrantTyreManufacturer
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantTyreManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season entrant tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonEntrantTyreManufacturerMapper : RecordUnmapper<SeasonEntrantTyreManufacturer, SeasonEntrantTyreManufacturerRecord> {

    override fun unmap(source: SeasonEntrantTyreManufacturer): SeasonEntrantTyreManufacturerRecord

    fun unmap(sources: List<SeasonEntrantTyreManufacturer>): List<SeasonEntrantTyreManufacturerRecord>
}
