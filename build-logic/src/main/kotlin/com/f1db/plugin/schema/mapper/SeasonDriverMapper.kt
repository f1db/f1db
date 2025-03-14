package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonDriver
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonDriver as SplittedSeasonDriver

/**
 * The season driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonDriverMapper {

    @Mapping(target = "year", ignore = true)
    fun toSplittedSeasonDriver(seasonDriver: SeasonDriver, @Context season: Season): SplittedSeasonDriver

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonDriver: SplittedSeasonDriver, @Context season: Season) {
        splittedSeasonDriver.year = season.year
    }
}
