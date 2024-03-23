package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.DriverOfTheDayResult
import com.f1db.plugin.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.DriverOfTheDayResult as SplittedDriverOfTheDayResult

/**
 * The driver of the day result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceDriverOfTheDayResultMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedDriverOfTheDayResult(driverOfTheDayResult: DriverOfTheDayResult, @Context race: Race, @Context index: Int): SplittedDriverOfTheDayResult

    @AfterMapping
    fun afterMapping(@MappingTarget splittedDriverOfTheDayResult: SplittedDriverOfTheDayResult, @Context race: Race, @Context index: Int) {
        splittedDriverOfTheDayResult.raceId = race.id
        splittedDriverOfTheDayResult.year = race.year
        splittedDriverOfTheDayResult.round = race.round
        splittedDriverOfTheDayResult.positionDisplayOrder = index
    }
}
