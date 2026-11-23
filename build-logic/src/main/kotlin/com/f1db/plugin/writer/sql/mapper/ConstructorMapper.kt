package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Constructor
import com.f1db.plugin.writer.sql.tables.records.ConstructorRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface ConstructorMapper : RecordUnmapper<Constructor, ConstructorRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    @Mapping(target = "fullNameNormalized", source = "fullName", qualifiedByName = ["normalize"])
    @Mapping(target = "total_1And_2Finishes", source = "total1And2Finishes")
    override fun unmap(source: Constructor): ConstructorRecord

    fun unmap(sources: List<Constructor>): List<ConstructorRecord>
}
