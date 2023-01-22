package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Season
import com.onlyf1.db.schema.single.SeasonEntrant
import com.onlyf1.db.schema.single.SeasonEntrantConstructor
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.SeasonEntrantConstructor as SplittedSeasonEntrantConstructor

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
