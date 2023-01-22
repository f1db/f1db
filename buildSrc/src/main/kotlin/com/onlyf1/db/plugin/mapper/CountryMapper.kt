package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Country
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Country as SplittedCountry

/**
 * The country mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface CountryMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(CountryMapper::class.java)
    }

    fun toSplittedCountry(country: Country): SplittedCountry

    fun toSplittedCountries(countries: List<Country>): List<SplittedCountry>
}
