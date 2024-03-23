package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Circuit
import com.f1db.plugin.writer.sql.tables.records.CircuitRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

/**
 * The circuit mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CircuitMapper : RecordUnmapper<Circuit, CircuitRecord> {

    @Mapping(target = "previousNames", source = "previousNames", qualifiedByName = ["previousNamesToString"])
    override fun unmap(source: Circuit): CircuitRecord

    fun unmap(sources: List<Circuit>): List<CircuitRecord>

    @Named("previousNamesToString")
    fun previousNamesToString(previousNames: List<String>?): String? = previousNames?.joinToString(";")
}
