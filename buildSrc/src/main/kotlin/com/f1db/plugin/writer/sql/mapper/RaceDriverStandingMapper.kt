package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.RaceDriverStanding
import com.f1db.plugin.writer.sql.tables.records.RaceDriverStandingRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The race driver standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface RaceDriverStandingMapper : RecordUnmapper<RaceDriverStanding, RaceDriverStandingRecord> {

    override fun unmap(source: RaceDriverStanding): RaceDriverStandingRecord

    fun unmap(sources: List<RaceDriverStanding>): List<RaceDriverStandingRecord>
}
