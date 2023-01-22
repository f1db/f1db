package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Race
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Race as SplittedRace

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
