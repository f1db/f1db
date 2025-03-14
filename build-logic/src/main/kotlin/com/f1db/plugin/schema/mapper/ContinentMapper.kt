package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Continent
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Continent as SplittedContinent

/**
 * The continent mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ContinentMapper {

    fun toSplittedContinent(continent: Continent): SplittedContinent

    fun toSplittedContinents(continents: List<Continent>): List<SplittedContinent>
}
