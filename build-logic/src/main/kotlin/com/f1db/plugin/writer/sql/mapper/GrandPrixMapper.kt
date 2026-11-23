package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.GrandPrix
import com.f1db.plugin.writer.sql.tables.records.GrandPrixRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The grand prix mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface GrandPrixMapper : RecordUnmapper<GrandPrix, GrandPrixRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    @Mapping(target = "fullNameNormalized", source = "fullName", qualifiedByName = ["normalize"])
    @Mapping(target = "shortNameNormalized", source = "shortName", qualifiedByName = ["normalize"])
    override fun unmap(source: GrandPrix): GrandPrixRecord

    fun unmap(sources: List<GrandPrix>): List<GrandPrixRecord>
}
