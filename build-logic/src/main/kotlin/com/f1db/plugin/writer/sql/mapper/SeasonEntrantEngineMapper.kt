package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrantEngine
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantEngineRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The season entrant engine mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonEntrantEngineMapper : RecordUnmapper<SeasonEntrantEngine, SeasonEntrantEngineRecord> {

    override fun unmap(source: SeasonEntrantEngine): SeasonEntrantEngineRecord

    fun unmap(sources: List<SeasonEntrantEngine>): List<SeasonEntrantEngineRecord>
}
