package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Race
import com.f1db.plugin.schema.single.RaceDriverStanding
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.RaceDriverStanding as SplittedRaceDriverStanding

/**
 * The race driver standing mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceDriverStandingMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    fun toSplittedRaceDriverStanding(raceDriverStanding: RaceDriverStanding, @Context race: Race): SplittedRaceDriverStanding

    fun toSplittedRaceDriverStandings(raceDriverStandings: List<RaceDriverStanding>, @Context race: Race): List<SplittedRaceDriverStanding>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedRaceDriverStanding: SplittedRaceDriverStanding, @Context race: Race) {
        splittedRaceDriverStanding.raceId = race.id
        splittedRaceDriverStanding.year = race.year
        splittedRaceDriverStanding.round = race.round
    }
}
