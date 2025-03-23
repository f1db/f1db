package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.ConstructorChronology
import com.f1db.plugin.writer.sql.tables.records.ConstructorChronologyRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The constructor chronology mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface ConstructorChronologyMapper : RecordUnmapper<ConstructorChronology, ConstructorChronologyRecord> {

    @Mapping(target = "constructorId", source = "parentConstructorId")
    @Mapping(target = "otherConstructorId", source = "constructorId")
    override fun unmap(source: ConstructorChronology): ConstructorChronologyRecord

    fun unmap(sources: List<ConstructorChronology>): List<ConstructorChronologyRecord>
}
