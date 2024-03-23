package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrant
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The season entrant mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonEntrantMapper : RecordUnmapper<SeasonEntrant, SeasonEntrantRecord> {

    override fun unmap(source: SeasonEntrant): SeasonEntrantRecord

    fun unmap(sources: List<SeasonEntrant>): List<SeasonEntrantRecord>
}
