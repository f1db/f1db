package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.CircuitLayout
import com.f1db.plugin.writer.sql.tables.records.CircuitLayoutRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The circuit layout mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface CircuitLayoutMapper : RecordUnmapper<CircuitLayout, CircuitLayoutRecord> {

    override fun unmap(source: CircuitLayout): CircuitLayoutRecord

    fun unmap(sources: List<CircuitLayout>): List<CircuitLayoutRecord>
}
