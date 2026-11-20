package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.TyreManufacturer
import com.f1db.plugin.writer.sql.tables.records.TyreManufacturerRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface TyreManufacturerMapper : RecordUnmapper<TyreManufacturer, TyreManufacturerRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    override fun unmap(source: TyreManufacturer): TyreManufacturerRecord

    fun unmap(sources: List<TyreManufacturer>): List<TyreManufacturerRecord>
}
