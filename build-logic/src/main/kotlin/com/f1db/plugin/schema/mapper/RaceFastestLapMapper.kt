package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.FastestLap
import com.f1db.plugin.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.FastestLap as SplittedFastestLap

/**
 * The fastest lap mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceFastestLapMapper {

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    fun toSplittedFastestLap(fastestLap: FastestLap, @Context race: Race): SplittedFastestLap

    fun toSplittedFastestLaps(fastestLaps: List<FastestLap>, @Context race: Race): List<SplittedFastestLap>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedFastestLap: SplittedFastestLap, @Context race: Race) {
        splittedFastestLap.raceId = race.id
        splittedFastestLap.year = race.year
        splittedFastestLap.round = race.round
    }
}
