package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Entrant
import com.f1db.plugin.writer.sql.tables.records.EntrantRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

/**
 * The entrant mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface EntrantMapper : RecordUnmapper<Entrant, EntrantRecord> {

    @Mapping(target = "nameNormalized", source = "name", qualifiedByName = ["normalize"])
    override fun unmap(source: Entrant): EntrantRecord

    fun unmap(sources: List<Entrant>): List<EntrantRecord>
}
