package com.f1db.plugin.mapper

import com.f1db.schema.single.Race
import com.f1db.schema.single.RaceResult
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.RaceResult as SplittedRaceResult

/**
 * The race result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceResultMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(RaceResultMapper::class.java)
    }

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedRaceResult(raceResult: RaceResult, @Context race: Race, @Context index: Int): SplittedRaceResult

    @AfterMapping
    fun afterMapping(@MappingTarget splittedRaceResult: SplittedRaceResult, @Context race: Race, @Context index: Int) {
        splittedRaceResult.raceId = race.id
        splittedRaceResult.year = race.year
        splittedRaceResult.round = race.round
        splittedRaceResult.positionDisplayOrder = index
    }
}
