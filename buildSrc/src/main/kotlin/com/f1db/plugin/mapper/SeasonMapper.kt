package com.f1db.plugin.mapper

import com.f1db.schema.single.Season
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Season as SplittedSeason

/**
 * The season mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(SeasonMapper::class.java)
    }

    fun toSplittedSeason(season: Season): SplittedSeason

    fun toSplittedSeasons(seasons: List<Season>): List<SplittedSeason>
}
