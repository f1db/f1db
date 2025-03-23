package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.PracticeResult
import com.f1db.plugin.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.PracticeResult as SplittedPracticeResult

/**
 * The practice result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RacePracticeResultMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    fun toSplittedPracticeResult(practiceResult: PracticeResult, @Context race: Race): SplittedPracticeResult

    fun toSplittedPracticeResults(practiceResults: List<PracticeResult>, @Context race: Race): List<SplittedPracticeResult>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedPracticeResult: SplittedPracticeResult, @Context race: Race) {
        splittedPracticeResult.raceId = race.id
        splittedPracticeResult.year = race.year
        splittedPracticeResult.round = race.round
    }
}
