package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Continent
import com.f1db.plugin.writer.sql.tables.records.ContinentRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The continent mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface ContinentMapper : RecordUnmapper<Continent, ContinentRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    override fun unmap(source: Continent): ContinentRecord

    fun unmap(sources: List<Continent>): List<ContinentRecord>
}
