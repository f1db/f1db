package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.QualifyingResult
import com.f1db.plugin.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.QualifyingResult as SplittedQualifyingResult

/**
 * The qualifying result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceQualifyingResultMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedQualifyingResult(qualifyingResult: QualifyingResult, @Context race: Race, @Context index: Int): SplittedQualifyingResult

    @AfterMapping
    fun afterMapping(@MappingTarget splittedQualifyingResult: SplittedQualifyingResult, @Context race: Race, @Context index: Int) {
        splittedQualifyingResult.raceId = race.id
        splittedQualifyingResult.year = race.year
        splittedQualifyingResult.round = race.round
        splittedQualifyingResult.positionDisplayOrder = index
    }
}
