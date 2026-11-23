package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Driver
import com.f1db.plugin.writer.sql.tables.records.DriverRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface DriverMapper : RecordUnmapper<Driver, DriverRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    @Mapping(target = "firstNameNormalized", source = "firstName", qualifiedByName = ["normalize"])
    @Mapping(target = "lastNameNormalized", source = "lastName", qualifiedByName = ["normalize"])
    @Mapping(target = "fullNameNormalized", source = "fullName", qualifiedByName = ["normalize"])
    @Mapping(target = "placeOfBirthNormalized", source = "placeOfBirth", qualifiedByName = ["normalize"])
    override fun unmap(source: Driver): DriverRecord

    fun unmap(sources: List<Driver>): List<DriverRecord>
}
