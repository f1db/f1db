package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Country
import com.f1db.plugin.writer.sql.tables.records.CountryRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The country mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface CountryMapper : RecordUnmapper<Country, CountryRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    override fun unmap(source: Country): CountryRecord

    fun unmap(sources: List<Country>): List<CountryRecord>
}
