package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Constructor
import com.f1db.plugin.schema.single.ConstructorPreviousNextConstructor
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.ConstructorPreviousNextConstructor as SplittedConstructorPreviousNextConstructor

/**
 * The constructor previous next constructor mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ConstructorPreviousNextConstructorMapper {

    @Mapping(target = "parentConstructorId", ignore = true)
    fun toSplittedPreviousNextConstructor(
        constructorPreviousNextConstructor: ConstructorPreviousNextConstructor,
        @Context parentConstructor: Constructor
    ): SplittedConstructorPreviousNextConstructor

    fun toSplittedPreviousNextConstructors(
        constructorPreviousNextConstructors: List<ConstructorPreviousNextConstructor>,
        @Context parentConstructor: Constructor
    ): List<SplittedConstructorPreviousNextConstructor>

    @AfterMapping
    fun afterMapping(
        @MappingTarget splittedConstructorPreviousNextConstructor: SplittedConstructorPreviousNextConstructor,
        @Context parentConstructor: Constructor
    ) {
        splittedConstructorPreviousNextConstructor.parentConstructorId = parentConstructor.id
    }
}
