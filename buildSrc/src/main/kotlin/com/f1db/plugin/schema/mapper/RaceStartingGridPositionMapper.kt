package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Race
import com.f1db.plugin.schema.single.StartingGridPosition
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.StartingGridPosition as SplittedStartingGridPosition

/**
 * The starting grid position mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceStartingGridPositionMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    fun toSplittedStartingGridPosition(startingGridPosition: StartingGridPosition, @Context race: Race): SplittedStartingGridPosition

    fun toSplittedStartingGridPositions(startingGridPositions: List<StartingGridPosition>, @Context race: Race): List<SplittedStartingGridPosition>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedStartingGridPosition: SplittedStartingGridPosition, @Context race: Race) {
        splittedStartingGridPosition.raceId = race.id
        splittedStartingGridPosition.year = race.year
        splittedStartingGridPosition.round = race.round
    }
}
