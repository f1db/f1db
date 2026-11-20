package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Circuit
import com.f1db.plugin.writer.sql.tables.records.CircuitRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

/**
 * The circuit mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface CircuitMapper : RecordUnmapper<Circuit, CircuitRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    @Mapping(target = "fullNameNormalized", source = "fullName", qualifiedByName = ["normalize"])
    @Mapping(target = "previousNames", source = "previousNames", qualifiedByName = ["previousNamesToString"])
    @Mapping(target = "previousNamesNormalized", source = "previousNames", qualifiedByName = ["previousNamesToNormalizedString"])
    @Mapping(target = "placeNameNormalized", source = "placeName", qualifiedByName = ["normalize"])
    override fun unmap(source: Circuit): CircuitRecord

    fun unmap(sources: List<Circuit>): List<CircuitRecord>

    @Named("previousNamesToString")
    fun previousNamesToString(previousNames: List<String>?): String? = previousNames?.joinToString(";")

    @Named("previousNamesToNormalizedString")
    fun previousNamesToNormalizedString(previousNames: List<String>?): String? =
        StringNormalizer.normalize(previousNamesToString(previousNames))
}
