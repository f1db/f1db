package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonDriverStanding
import com.f1db.plugin.writer.sql.tables.records.SeasonDriverStandingRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season driver standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonDriverStandingMapper : RecordUnmapper<SeasonDriverStanding, SeasonDriverStandingRecord> {

    override fun unmap(source: SeasonDriverStanding): SeasonDriverStandingRecord

    fun unmap(sources: List<SeasonDriverStanding>): List<SeasonDriverStandingRecord>
}
