package com.f1db.plugin.mapper

import com.f1db.schema.single.TyreManufacturer
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.TyreManufacturer as SplittedTyreManufacturer

/**
 * The tyre manufacturer mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface TyreManufacturerMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(TyreManufacturerMapper::class.java)
    }

    fun toSplittedTyreManufacturer(tyreManufacturer: TyreManufacturer): SplittedTyreManufacturer

    fun toSplittedTyreManufacturers(tyreManufacturers: List<TyreManufacturer>): List<SplittedTyreManufacturer>
}
