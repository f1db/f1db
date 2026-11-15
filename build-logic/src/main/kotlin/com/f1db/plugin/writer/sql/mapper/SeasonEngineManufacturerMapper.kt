package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEngineManufacturer
import com.f1db.plugin.writer.sql.tables.records.SeasonEngineManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season engine manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonEngineManufacturerMapper : RecordUnmapper<SeasonEngineManufacturer, SeasonEngineManufacturerRecord> {

    override fun unmap(source: SeasonEngineManufacturer): SeasonEngineManufacturerRecord

    fun unmap(sources: List<SeasonEngineManufacturer>): List<SeasonEngineManufacturerRecord>
}
