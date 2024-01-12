package com.f1db.plugin.mapper

import com.f1db.schema.single.Season
import com.f1db.schema.single.SeasonEntrant
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.SeasonEntrant as SplittedSeasonEntrant

/**
 * The season entrant mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonEntrantMapper::class.java)
    }

    @Mapping(target = "year", ignore = true)
    fun toSplittedSeasonEntrant(seasonEntrant: SeasonEntrant, @Context season: Season): SplittedSeasonEntrant

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonEntrant: SplittedSeasonEntrant, @Context season: Season) {
        splittedSeasonEntrant.year = season.year
    }
}
