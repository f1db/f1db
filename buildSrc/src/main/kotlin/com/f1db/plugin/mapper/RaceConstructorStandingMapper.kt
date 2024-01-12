package com.f1db.plugin.mapper

import com.f1db.schema.single.Race
import com.f1db.schema.single.RaceConstructorStanding
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.RaceConstructorStanding as SplittedRaceConstructorStanding

/**
 * The race constructor standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceConstructorStandingMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(RaceConstructorStandingMapper::class.java)
    }

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedRaceConstructorStanding(
        raceConstructorStanding: RaceConstructorStanding,
        @Context race: Race,
        @Context index: Int
    ): SplittedRaceConstructorStanding

    @AfterMapping
    fun afterMapping(@MappingTarget splittedRaceConstructorStanding: SplittedRaceConstructorStanding, @Context race: Race, @Context index: Int) {
        splittedRaceConstructorStanding.raceId = race.id
        splittedRaceConstructorStanding.year = race.year
        splittedRaceConstructorStanding.round = race.round
        splittedRaceConstructorStanding.positionDisplayOrder = index
    }
}
