package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.PitStop
import com.f1db.plugin.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.PitStop as SplittedPitStop

/**
 * The pit stop mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RacePitStopMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedPitStop(pitStop: PitStop, @Context race: Race, @Context index: Int): SplittedPitStop

    @AfterMapping
    fun afterMapping(@MappingTarget splittedPitStop: SplittedPitStop, @Context race: Race, @Context index: Int) {
        splittedPitStop.raceId = race.id
        splittedPitStop.year = race.year
        splittedPitStop.round = race.round
        splittedPitStop.positionDisplayOrder = index
    }
}
