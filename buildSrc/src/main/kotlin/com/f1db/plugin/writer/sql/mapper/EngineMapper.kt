package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.splitted.Engine
import com.f1db.plugin.writer.sql.tables.records.EngineRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named
import org.mapstruct.ReportingPolicy

/**
 * The engine mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface EngineMapper : RecordUnmapper<Engine, EngineRecord> {

    @Mapping(target = "configuration", source = "configuration", qualifiedByName = ["mapConfigurationEnumToString"])
    override fun unmap(source: Engine): EngineRecord

    fun unmap(sources: List<Engine>): List<EngineRecord>

    @Named("mapConfigurationEnumToString")
    fun map(configuration: Engine.EngineConfiguration?): String? {
        return configuration?.value()
    }
}
