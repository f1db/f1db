package com.f1db.plugin.mapper

import com.f1db.schema.single.Season
import com.f1db.schema.single.SeasonEntrant
import com.f1db.schema.single.SeasonEntrantConstructor
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.SeasonEntrantConstructor as SplittedSeasonEntrantConstructor

/**
 * The season entrant constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantConstructorMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonEntrantConstructorMapper::class.java)
    }

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "entrantId", ignore = true)
    fun toSplittedSeasonEntrantConstructor(
        seasonEntrantConstructor: SeasonEntrantConstructor,
        @Context season: Season,
        @Context seasonEntrant: SeasonEntrant
    ): SplittedSeasonEntrantConstructor

    @AfterMapping
    fun afterMapping(
        @MappingTarget splittedSeasonEntrantConstructor: SplittedSeasonEntrantConstructor,
        @Context season: Season,
        @Context seasonEntrant: SeasonEntrant
    ) {
        splittedSeasonEntrantConstructor.year = season.year
        splittedSeasonEntrantConstructor.entrantId = seasonEntrant.entrantId
    }
}
