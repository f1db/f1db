package com.f1db.plugin.mapper

import com.f1db.schema.single.EngineManufacturer
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.EngineManufacturer as SplittedEngineManufacturer

/**
 * The engine manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface EngineManufacturerMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(EngineManufacturerMapper::class.java)
    }

    fun toSplittedEngineManufacturer(engineManufacturer: EngineManufacturer): SplittedEngineManufacturer

    fun toSplittedEngineManufacturers(engineManufacturers: List<EngineManufacturer>): List<SplittedEngineManufacturer>
}
