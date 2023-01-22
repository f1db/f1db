package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Season
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Season as SplittedSeason

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
