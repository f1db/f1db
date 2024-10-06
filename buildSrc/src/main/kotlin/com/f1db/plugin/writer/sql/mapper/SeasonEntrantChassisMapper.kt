package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrantChassis
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantChassisRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The season entrant chassis mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonEntrantChassisMapper : RecordUnmapper<SeasonEntrantChassis, SeasonEntrantChassisRecord> {

    override fun unmap(source: SeasonEntrantChassis): SeasonEntrantChassisRecord

    fun unmap(sources: List<SeasonEntrantChassis>): List<SeasonEntrantChassisRecord>
}
