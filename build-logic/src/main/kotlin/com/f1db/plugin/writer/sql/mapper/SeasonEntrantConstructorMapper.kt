package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrantConstructor
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantConstructorRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The season entrant constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonEntrantConstructorMapper : RecordUnmapper<SeasonEntrantConstructor, SeasonEntrantConstructorRecord> {

    override fun unmap(source: SeasonEntrantConstructor): SeasonEntrantConstructorRecord

    fun unmap(sources: List<SeasonEntrantConstructor>): List<SeasonEntrantConstructorRecord>
}
