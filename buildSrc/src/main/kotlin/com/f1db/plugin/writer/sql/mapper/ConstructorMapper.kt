package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Constructor
import com.f1db.plugin.writer.sql.tables.records.ConstructorRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ConstructorMapper : RecordUnmapper<Constructor, ConstructorRecord> {

    @Mapping(target = "total_1And_2Finishes", source = "total1And2Finishes")
    override fun unmap(source: Constructor): ConstructorRecord

    fun unmap(sources: List<Constructor>): List<ConstructorRecord>
}
