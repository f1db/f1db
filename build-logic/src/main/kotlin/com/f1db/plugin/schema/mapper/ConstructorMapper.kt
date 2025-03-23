package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Constructor
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Constructor as SplittedConstructor

/**
 * The constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ConstructorMapper {

    fun toSplittedConstructor(constructor: Constructor): SplittedConstructor

    fun toSplittedConstructors(constructors: List<Constructor>): List<SplittedConstructor>
}
