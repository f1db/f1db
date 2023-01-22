package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.QualifyingResult
import com.onlyf1.db.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.QualifyingResult as SplittedQualifyingResult

/**
 * The qualifying result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface QualifyingResultMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(QualifyingResultMapper::class.java)
    }

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
