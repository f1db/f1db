package com.f1db.plugin.mapper

import com.f1db.schema.single.Continent
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Continent as SplittedContinent

/**
 * The continent mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ContinentMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(ContinentMapper::class.java)
    }

    fun toSplittedContinent(continent: Continent): SplittedContinent

    fun toSplittedContinents(continents: List<Continent>): List<SplittedContinent>
}
