package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Constructor
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Constructor as SplittedConstructor

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
