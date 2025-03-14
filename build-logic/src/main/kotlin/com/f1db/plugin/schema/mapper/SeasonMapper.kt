package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Season as SplittedSeason

/**
 * The season mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonMapper {

    fun toSplittedSeason(season: Season): SplittedSeason

    fun toSplittedSeasons(seasons: List<Season>): List<SplittedSeason>
}
