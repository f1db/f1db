package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Engine
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Engine as SplittedEngine

/**
 * The engine mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface EngineMapper {

    fun toSplittedEngine(engine: Engine): SplittedEngine

    fun toSplittedEngines(engines: List<Engine>): List<SplittedEngine>
}
