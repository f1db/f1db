package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Race
import com.f1db.plugin.schema.single.RaceConstructorStanding
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.RaceConstructorStanding as SplittedRaceConstructorStanding

/**
 * The race constructor standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceConstructorStandingMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    fun toSplittedRaceConstructorStanding(raceConstructorStanding: RaceConstructorStanding, @Context race: Race): SplittedRaceConstructorStanding

    fun toSplittedRaceConstructorStandings(raceConstructorStandings: List<RaceConstructorStanding>, @Context race: Race): List<SplittedRaceConstructorStanding>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedRaceConstructorStanding: SplittedRaceConstructorStanding, @Context race: Race) {
        splittedRaceConstructorStanding.raceId = race.id
        splittedRaceConstructorStanding.year = race.year
        splittedRaceConstructorStanding.round = race.round
    }
}
