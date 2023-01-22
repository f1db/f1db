package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Season
import com.onlyf1.db.schema.single.SeasonEntrant
import com.onlyf1.db.schema.single.SeasonEntrantConstructor
import com.onlyf1.db.schema.single.SeasonEntrantDriver
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.SeasonEntrantDriver as SplittedSeasonEntrantDriver

/**
 * The season entrant driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantDriverMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonEntrantDriverMapper::class.java)
    }

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "entrantId", ignore = true)
    @Mapping(target = "constructorId", ignore = true)
    @Mapping(target = "engineManufacturerId", ignore = true)
    fun toSplittedSeasonEntrantDriver(
        seasonEntrantDriver: SeasonEntrantDriver,
        @Context season: Season,
        @Context seasonEntrant: SeasonEntrant,
        @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ): SplittedSeasonEntrantDriver

    @AfterMapping
    fun afterMapping(
        @MappingTarget splittedSeasonEntrantDriver: SplittedSeasonEntrantDriver,
        @Context season: Season,
        @Context seasonEntrant: SeasonEntrant,
        @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ) {
        splittedSeasonEntrantDriver.year = season.year
        splittedSeasonEntrantDriver.entrantId = seasonEntrant.entrantId
        splittedSeasonEntrantDriver.constructorId = seasonEntrantConstructor.constructorId
        splittedSeasonEntrantDriver.engineManufacturerId = seasonEntrantConstructor.engineManufacturerId
    }
}
