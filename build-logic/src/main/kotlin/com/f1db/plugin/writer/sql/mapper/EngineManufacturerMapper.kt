package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.EngineManufacturer
import com.f1db.plugin.writer.sql.tables.records.EngineManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The engine manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface EngineManufacturerMapper : RecordUnmapper<EngineManufacturer, EngineManufacturerRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    override fun unmap(source: EngineManufacturer): EngineManufacturerRecord

    fun unmap(sources: List<EngineManufacturer>): List<EngineManufacturerRecord>
}
