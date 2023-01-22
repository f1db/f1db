package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.EngineManufacturer
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.EngineManufacturer as SplittedEngineManufacturer

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
