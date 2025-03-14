package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonEntrantDriver
import com.f1db.plugin.writer.sql.tables.records.SeasonEntrantDriverRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

/**
 * The season entrant driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface SeasonEntrantDriverMapper : RecordUnmapper<SeasonEntrantDriver, SeasonEntrantDriverRecord> {

    @Mapping(target = "rounds", source = "rounds", qualifiedByName = ["roundsToString"])
    override fun unmap(source: SeasonEntrantDriver): SeasonEntrantDriverRecord

    fun unmap(sources: List<SeasonEntrantDriver>): List<SeasonEntrantDriverRecord>

    @Named("roundsToString")
    fun roundsToString(rounds: List<Int>?): String? = rounds?.joinToString(";")
}
