package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonConstructor
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.SeasonConstructor as SplittedSeasonConstructor

/**
 * The season constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface SeasonConstructorMapper {

    @Mapping(target = "year", ignore = true)
    fun toSplittedSeasonConstructor(seasonConstructor: SeasonConstructor, @Context season: Season): SplittedSeasonConstructor

    @AfterMapping
    fun afterMapping(@MappingTarget splittedSeasonConstructor: SplittedSeasonConstructor, @Context season: Season) {
        splittedSeasonConstructor.year = season.year
    }
}
