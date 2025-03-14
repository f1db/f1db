package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonEngineManufacturer
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonEngineManufacturer as SplittedSeasonEngineManufacturer

/**
 * The season engine manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEngineManufacturerMapper {

    @Mapping(target = "year", ignore = true)
    fun toSplittedSeasonEngineManufacturer(seasonEngineManufacturer: SeasonEngineManufacturer, @Context season: Season): SplittedSeasonEngineManufacturer

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonEngineManufacturer: SplittedSeasonEngineManufacturer, @Context season: Season) {
        splittedSeasonEngineManufacturer.year = season.year
    }
}
