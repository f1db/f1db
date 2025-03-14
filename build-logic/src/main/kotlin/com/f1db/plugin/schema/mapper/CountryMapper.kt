package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Country
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Country as SplittedCountry

/**
 * The country mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface CountryMapper {

    fun toSplittedCountry(country: Country): SplittedCountry

    fun toSplittedCountries(countries: List<Country>): List<SplittedCountry>
}
