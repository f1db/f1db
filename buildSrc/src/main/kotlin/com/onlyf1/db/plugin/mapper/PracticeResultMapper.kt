package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.PracticeResult
import com.onlyf1.db.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.PracticeResult as SplittedPracticeResult

/**
 * The practice result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface PracticeResultMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(PracticeResultMapper::class.java)
    }

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedPracticeResult(practiceResult: PracticeResult, @Context race: Race, @Context index: Int): SplittedPracticeResult

    @AfterMapping
    fun afterMapping(@MappingTarget splittedPracticeResult: SplittedPracticeResult, @Context race: Race, @Context index: Int) {
        splittedPracticeResult.raceId = race.id
        splittedPracticeResult.year = race.year
        splittedPracticeResult.round = race.round
        splittedPracticeResult.positionDisplayOrder = index
    }
}
