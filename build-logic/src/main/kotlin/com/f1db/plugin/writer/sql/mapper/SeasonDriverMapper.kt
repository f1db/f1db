package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.SeasonDriver
import com.f1db.plugin.writer.sql.tables.records.SeasonDriverRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper

/**
 * The season driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(config = BaseMapperConfig::class)
interface SeasonDriverMapper : RecordUnmapper<SeasonDriver, SeasonDriverRecord> {

    override fun unmap(source: SeasonDriver): SeasonDriverRecord

    fun unmap(sources: List<SeasonDriver>): List<SeasonDriverRecord>
}
