package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Race
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Race as SplittedRace

/**
 * The race mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceMapper {

    fun toSplittedRace(race: Race): SplittedRace

    fun toSplittedRaces(race: List<Race>): List<SplittedRace>
}
