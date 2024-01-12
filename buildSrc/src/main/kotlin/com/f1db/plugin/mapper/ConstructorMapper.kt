package com.f1db.plugin.mapper

import com.f1db.schema.single.Constructor
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Constructor as SplittedConstructor

/**
 * The constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ConstructorMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(ConstructorMapper::class.java)
    }

    fun toSplittedConstructor(constructor: Constructor): SplittedConstructor

    fun toSplittedConstructors(constructors: List<Constructor>): List<SplittedConstructor>
}
