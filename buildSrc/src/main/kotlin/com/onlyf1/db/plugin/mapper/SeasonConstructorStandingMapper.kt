package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Season
import com.onlyf1.db.schema.single.SeasonConstructorStanding
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.SeasonConstructorStanding as SplittedSeasonConstructorStanding

/**
 * The season constructor standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonConstructorStandingMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonConstructorStandingMapper::class.java)
    }

    @Mapping(target = "year", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedSeasonConstructorStanding(
        seasonConstructorStanding: SeasonConstructorStanding,
        @Context season: Season,
        @Context index: Int
    ): SplittedSeasonConstructorStanding

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonConstructorStanding: SplittedSeasonConstructorStanding, @Context season: Season, @Context index: Int) {
        splittedSeasonConstructorStanding.year = season.year
        splittedSeasonConstructorStanding.positionDisplayOrder = index
    }
}
