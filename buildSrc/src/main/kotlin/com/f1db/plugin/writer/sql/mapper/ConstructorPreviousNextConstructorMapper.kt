package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.ConstructorPreviousNextConstructor
import com.f1db.plugin.writer.sql.tables.records.ConstructorPreviousNextConstructorRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The constructor previous next constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ConstructorPreviousNextConstructorMapper : RecordUnmapper<ConstructorPreviousNextConstructor, ConstructorPreviousNextConstructorRecord> {

    @Mapping(target = "constructorId", source = "parentConstructorId")
    @Mapping(target = "previousNextConstructorId", source = "constructorId")
    override fun unmap(source: ConstructorPreviousNextConstructor): ConstructorPreviousNextConstructorRecord

    fun unmap(sources: List<ConstructorPreviousNextConstructor>): List<ConstructorPreviousNextConstructorRecord>
}
