package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonConstructor
import com.f1db.plugin.writer.sql.tables.records.SeasonConstructorRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The season constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonConstructorMapper : RecordUnmapper<SeasonConstructor, SeasonConstructorRecord> {

    @Mapping(target = "total_1And_2Finishes", source = "total1And2Finishes")
    override fun unmap(source: SeasonConstructor): SeasonConstructorRecord

    fun unmap(sources: List<SeasonConstructor>): List<SeasonConstructorRecord>
}
