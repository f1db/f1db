package com.f1db.plugin.mapper

import com.f1db.schema.single.DriverOfTheDayResult
import com.f1db.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.DriverOfTheDayResult as SplittedDriverOfTheDayResult

/**
 * The driver of the day result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface DriverOfTheDayResultMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(DriverOfTheDayResultMapper::class.java)
    }

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
