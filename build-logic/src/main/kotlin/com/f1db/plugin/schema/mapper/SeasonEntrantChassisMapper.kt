package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonEntrant
import com.f1db.plugin.schema.single.SeasonEntrantChassis
import com.f1db.plugin.schema.single.SeasonEntrantConstructor
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonEntrantChassis as SplittedSeasonEntrantChassis

/**
 * The season entrant chassis mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantChassisMapper {

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "entrantId", ignore = true)
    @Mapping(target = "constructorId", ignore = true)
    @Mapping(target = "engineManufacturerId", ignore = true)
    fun toSplittedSeasonEntrantChassis(
            seasonEntrantChassis: SeasonEntrantChassis,
            @Context season: Season,
            @Context seasonEntrant: SeasonEntrant,
            @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ): SplittedSeasonEntrantChassis

    @AfterMapping
    fun afterMapping(
            @MappingTarget splittedSeasonEntrantChassis: SplittedSeasonEntrantChassis,
            @Context season: Season,
            @Context seasonEntrant: SeasonEntrant,
            @Context seasonEntrantConstructor: SeasonEntrantConstructor
    ) {
        splittedSeasonEntrantChassis.year = season.year
        splittedSeasonEntrantChassis.entrantId = seasonEntrant.entrantId
        splittedSeasonEntrantChassis.constructorId = seasonEntrantConstructor.constructorId
        splittedSeasonEntrantChassis.engineManufacturerId = seasonEntrantConstructor.engineManufacturerId
    }
}
