package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.GrandPrix
import com.f1db.plugin.writer.sql.tables.records.GrandPrixRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

/**
 * The grand prix mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface GrandPrixMapper : RecordUnmapper<GrandPrix, GrandPrixRecord> {

    override fun unmap(source: GrandPrix): GrandPrixRecord

    fun unmap(sources: List<GrandPrix>): List<GrandPrixRecord>
}
