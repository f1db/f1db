package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonDriverStanding
import com.f1db.plugin.writer.sql.tables.records.SeasonDriverStandingRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The season driver standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonDriverStandingMapper : RecordUnmapper<SeasonDriverStanding, SeasonDriverStandingRecord> {

    override fun unmap(source: SeasonDriverStanding): SeasonDriverStandingRecord

    fun unmap(sources: List<SeasonDriverStanding>): List<SeasonDriverStandingRecord>
}
