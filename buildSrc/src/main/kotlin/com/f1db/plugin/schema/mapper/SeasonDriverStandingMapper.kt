package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonDriverStanding
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonDriverStanding as SplittedSeasonDriverStanding

/**
 * The season driver standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonDriverStandingMapper {

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedSeasonDriverStanding(seasonDriverStanding: SeasonDriverStanding, @Context season: Season, @Context index: Int): SplittedSeasonDriverStanding

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonDriverStanding: SplittedSeasonDriverStanding, @Context season: Season, @Context index: Int) {
        splittedSeasonDriverStanding.year = season.year
        splittedSeasonDriverStanding.positionDisplayOrder = index
    }
}
