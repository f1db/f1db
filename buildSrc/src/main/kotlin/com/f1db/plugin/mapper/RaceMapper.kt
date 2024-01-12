package com.f1db.plugin.mapper

import com.f1db.schema.single.Race
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Race as SplittedRace

/**
 * The race mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface RaceMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(RaceMapper::class.java)
    }

    fun toSplittedRace(race: Race): SplittedRace

    fun toSplittedRaces(race: List<Race>): List<SplittedRace>
}
