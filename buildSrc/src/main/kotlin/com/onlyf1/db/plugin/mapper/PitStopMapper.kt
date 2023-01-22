package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.PitStop
import com.onlyf1.db.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.PitStop as SplittedPitStop

/**
 * The pit stop mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface PitStopMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(PitStopMapper::class.java)
    }

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
