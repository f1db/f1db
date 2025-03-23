package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Race
import com.f1db.plugin.schema.single.RaceResult
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.RaceResult as SplittedRaceResult

/**
 * The race result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceRaceResultMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    fun toSplittedRaceResult(raceResult: RaceResult, @Context race: Race): SplittedRaceResult

    fun toSplittedRaceResults(raceResults: List<RaceResult>, @Context race: Race): List<SplittedRaceResult>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedRaceResult: SplittedRaceResult, @Context race: Race) {
        splittedRaceResult.raceId = race.id
        splittedRaceResult.year = race.year
        splittedRaceResult.round = race.round
    }
}
