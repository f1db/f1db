package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.FastestLap
import com.onlyf1.db.schema.single.Race
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.FastestLap as SplittedFastestLap

/**
 * The fastest lap mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface FastestLapMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(FastestLapMapper::class.java)
    }

    @Mapping(target = "raceId", ignore = true)
    @Mapping(target = "year", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "positionDisplayOrder", ignore = true)
    fun toSplittedFastestLap(fastestLap: FastestLap, @Context race: Race, @Context index: Int): SplittedFastestLap

    @AfterMapping
    fun afterMapping(@MappingTarget splittedFastestLap: SplittedFastestLap, @Context race: Race, @Context index: Int) {
        splittedFastestLap.raceId = race.id
        splittedFastestLap.year = race.year
        splittedFastestLap.round = race.round
        splittedFastestLap.positionDisplayOrder = index
    }
}
