package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrantEngine
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantEngineRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season entrant engine mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonEntrantEngineMapper : RecordUnmapper<SeasonEntrantEngine, SeasonEntrantEngineRecord> {

    override fun unmap(source: SeasonEntrantEngine): SeasonEntrantEngineRecord

    fun unmap(sources: List<SeasonEntrantEngine>): List<SeasonEntrantEngineRecord>
}
