package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Season
import com.onlyf1.db.schema.single.SeasonEntrant
import com.onlyf1.db.schema.single.SeasonEntrantConstructor
import com.onlyf1.db.schema.single.SeasonEntrantTyreManufacturer
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.SeasonEntrantTyreManufacturer as SplittedSeasonEntrantTyreManufacturer

/**
 * The season entrant tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantTyreManufacturerMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonEntrantTyreManufacturerMapper::class.java)
    }

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
