package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonEntrant
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonEntrant as SplittedSeasonEntrant

/**
 * The season entrant mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonEntrantMapper {

    @Mapping(target = "year", ignore = true)
    fun toSplittedSeasonEntrant(seasonEntrant: SeasonEntrant, @Context season: Season): SplittedSeasonEntrant

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonEntrant: SplittedSeasonEntrant, @Context season: Season) {
        splittedSeasonEntrant.year = season.year
    }
}
