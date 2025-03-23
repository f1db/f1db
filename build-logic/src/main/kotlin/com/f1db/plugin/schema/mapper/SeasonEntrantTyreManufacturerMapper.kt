package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonEntrant
import com.f1db.plugin.schema.single.SeasonEntrantConstructor
import com.f1db.plugin.schema.single.SeasonEntrantTyreManufacturer
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonEntrantTyreManufacturer as SplittedSeasonEntrantTyreManufacturer

/**
 * The season entrant tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantTyreManufacturerMapper {

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "entrantId", ignore = true)
    @Mapping(target = "constructorId", ignore = true)
    @Mapping(target = "engineManufacturerId", ignore = true)
    fun toSplittedSeasonEntrantTyreManufacturer(
            seasonEntrantTyreManufacturer: SeasonEntrantTyreManufacturer,
            @Context season: Season,
            @Context seasonEntrant: SeasonEntrant,
            @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ): SplittedSeasonEntrantTyreManufacturer

    @AfterMapping
    fun afterMapping(
            @MappingTarget splittedSeasonEntrantTyreManufacturer: SplittedSeasonEntrantTyreManufacturer,
            @Context season: Season,
            @Context seasonEntrant: SeasonEntrant,
            @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ) {
        splittedSeasonEntrantTyreManufacturer.year = season.year
        splittedSeasonEntrantTyreManufacturer.entrantId = seasonEntrant.entrantId
        splittedSeasonEntrantTyreManufacturer.constructorId = seasonEntrantConstructor.constructorId
        splittedSeasonEntrantTyreManufacturer.engineManufacturerId = seasonEntrantConstructor.engineManufacturerId
    }
}
