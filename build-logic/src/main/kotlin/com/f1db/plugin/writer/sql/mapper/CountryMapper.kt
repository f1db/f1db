package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Country
import com.f1db.plugin.writer.sql.tables.records.CountryRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The country mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CountryMapper : RecordUnmapper<Country, CountryRecord> {

    override fun unmap(source: Country): CountryRecord

    fun unmap(sources: List<Country>): List<CountryRecord>
}
