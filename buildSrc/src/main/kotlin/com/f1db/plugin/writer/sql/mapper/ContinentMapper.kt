package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Continent
import com.f1db.plugin.writer.sql.tables.records.ContinentRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The continent mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ContinentMapper : RecordUnmapper<Continent, ContinentRecord> {

    override fun unmap(source: Continent): ContinentRecord

    fun unmap(sources: List<Continent>): List<ContinentRecord>
}
