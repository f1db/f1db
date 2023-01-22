package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.TyreManufacturer
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.TyreManufacturer as SplittedTyreManufacturer

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
