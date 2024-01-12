package com.f1db.plugin.mapper

import com.f1db.schema.single.Country
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Country as SplittedCountry

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
