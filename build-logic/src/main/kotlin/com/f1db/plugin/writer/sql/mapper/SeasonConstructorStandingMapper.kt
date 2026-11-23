package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonConstructorStanding
import com.f1db.plugin.writer.sql.tables.records.SeasonConstructorStandingRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season constructor standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonConstructorStandingMapper : RecordUnmapper<SeasonConstructorStanding, SeasonConstructorStandingRecord> {

    override fun unmap(source: SeasonConstructorStanding): SeasonConstructorStandingRecord

    fun unmap(sources: List<SeasonConstructorStanding>): List<SeasonConstructorStandingRecord>
}
