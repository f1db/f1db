package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonTyreManufacturer
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonTyreManufacturer as SplittedSeasonTyreManufacturer

/**
 * The season tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonTyreManufacturerMapper {

    @Mapping(target = "year", ignore = true)
    fun toSplittedSeasonTyreManufacturer(seasonTyreManufacturer: SeasonTyreManufacturer, @Context season: Season): SplittedSeasonTyreManufacturer

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonTyreManufacturer: SplittedSeasonTyreManufacturer, @Context season: Season) {
        splittedSeasonTyreManufacturer.year = season.year
    }
}
