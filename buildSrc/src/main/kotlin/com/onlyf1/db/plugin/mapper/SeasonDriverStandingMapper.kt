package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Season
import com.onlyf1.db.schema.single.SeasonDriverStanding
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.SeasonDriverStanding as SplittedSeasonDriverStanding

/**
 * The season driver standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonDriverStandingMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonDriverStandingMapper::class.java)
    }

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedSeasonDriverStanding(seasonDriverStanding: SeasonDriverStanding, @Context season: Season, @Context index: Int): SplittedSeasonDriverStanding

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonDriverStanding: SplittedSeasonDriverStanding, @Context season: Season, @Context index: Int) {
        splittedSeasonDriverStanding.year = season.year
        splittedSeasonDriverStanding.positionDisplayOrder = index
    }
}
