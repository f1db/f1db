package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonEntrant
import com.f1db.plugin.schema.single.SeasonEntrantConstructor
import com.f1db.plugin.schema.single.SeasonEntrantEngine
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonEntrantEngine as SplittedSeasonEntrantEngine

/**
 * The season entrant engine mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantEngineMapper {

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "entrantId", ignore = true)
    @Mapping(target = "constructorId", ignore = true)
    @Mapping(target = "engineManufacturerId", ignore = true)
    fun toSplittedSeasonEntrantEngine(
            seasonEntrantEngine: SeasonEntrantEngine,
            @Context season: Season,
            @Context seasonEntrant: SeasonEntrant,
            @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ): SplittedSeasonEntrantEngine

    @AfterMapping
    fun afterMapping(
            @MappingTarget splittedSeasonEntrantEngine: SplittedSeasonEntrantEngine,
            @Context season: Season,
            @Context seasonEntrant: SeasonEntrant,
            @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ) {
        splittedSeasonEntrantEngine.year = season.year
        splittedSeasonEntrantEngine.entrantId = seasonEntrant.entrantId
        splittedSeasonEntrantEngine.constructorId = seasonEntrantConstructor.constructorId
        splittedSeasonEntrantEngine.engineManufacturerId = seasonEntrantConstructor.engineManufacturerId
    }
}
