package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.TyreManufacturer
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.TyreManufacturer as SplittedTyreManufacturer

/**
 * The tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface TyreManufacturerMapper {

    fun toSplittedTyreManufacturer(tyreManufacturer: TyreManufacturer): SplittedTyreManufacturer

    fun toSplittedTyreManufacturers(tyreManufacturers: List<TyreManufacturer>): List<SplittedTyreManufacturer>
}
