package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.EngineManufacturer
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.EngineManufacturer as SplittedEngineManufacturer

/**
 * The engine manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface EngineManufacturerMapper {

    fun toSplittedEngineManufacturer(engineManufacturer: EngineManufacturer): SplittedEngineManufacturer

    fun toSplittedEngineManufacturers(engineManufacturers: List<EngineManufacturer>): List<SplittedEngineManufacturer>
}
