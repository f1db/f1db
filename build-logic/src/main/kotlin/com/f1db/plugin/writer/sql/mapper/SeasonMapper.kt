package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Season
import com.f1db.plugin.writer.sql.tables.records.SeasonRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonMapper : RecordUnmapper<Season, SeasonRecord> {

    override fun unmap(source: Season): SeasonRecord

    fun unmap(sources: List<Season>): List<SeasonRecord>
}
