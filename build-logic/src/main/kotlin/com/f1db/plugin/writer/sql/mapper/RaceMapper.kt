package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Race
import com.f1db.plugin.writer.sql.tables.records.RaceRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The race mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface RaceMapper : RecordUnmapper<Race, RaceRecord> {

    @Mapping(target = "freePractice_1Date", source = "freePractice1Date")
    @Mapping(target = "freePractice_1Time", source = "freePractice1Time")
    @Mapping(target = "freePractice_2Date", source = "freePractice2Date")
    @Mapping(target = "freePractice_2Time", source = "freePractice2Time")
    @Mapping(target = "freePractice_3Date", source = "freePractice3Date")
    @Mapping(target = "freePractice_3Time", source = "freePractice3Time")
    @Mapping(target = "freePractice_4Date", source = "freePractice4Date")
    @Mapping(target = "freePractice_4Time", source = "freePractice4Time")
    @Mapping(target = "qualifying_1Date", source = "qualifying1Date")
    @Mapping(target = "qualifying_1Time", source = "qualifying1Time")
    @Mapping(target = "qualifying_2Date", source = "qualifying2Date")
    @Mapping(target = "qualifying_2Time", source = "qualifying2Time")
    override fun unmap(source: Race): RaceRecord

    fun unmap(sources: List<Race>): List<RaceRecord>
}
