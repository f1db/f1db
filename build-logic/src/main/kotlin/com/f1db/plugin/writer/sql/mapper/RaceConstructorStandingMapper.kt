package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.RaceConstructorStanding
import com.f1db.plugin.writer.sql.tables.records.RaceConstructorStandingRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The race constructor standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface RaceConstructorStandingMapper : RecordUnmapper<RaceConstructorStanding, RaceConstructorStandingRecord> {

    override fun unmap(source: RaceConstructorStanding): RaceConstructorStandingRecord

    fun unmap(sources: List<RaceConstructorStanding>): List<RaceConstructorStandingRecord>
}
