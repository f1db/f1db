package com.f1db.plugin.mapper

import com.f1db.schema.single.Race
import com.f1db.schema.single.StartingGridPosition
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.StartingGridPosition as SplittedStartingGridPosition

/**
 * The starting grid position mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface StartingGridPositionMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(StartingGridPositionMapper::class.java)
    }

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedStartingGridPosition(startingGridPosition: StartingGridPosition, @Context race: Race, @Context index: Int): SplittedStartingGridPosition

    @AfterMapping
    fun afterMapping(@MappingTarget splittedStartingGridPosition: SplittedStartingGridPosition, @Context race: Race, @Context index: Int) {
        splittedStartingGridPosition.raceId = race.id
        splittedStartingGridPosition.year = race.year
        splittedStartingGridPosition.round = race.round
        splittedStartingGridPosition.positionDisplayOrder = index
    }
}
