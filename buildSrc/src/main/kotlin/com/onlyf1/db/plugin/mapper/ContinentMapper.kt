package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Continent
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Continent as SplittedContinent

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
