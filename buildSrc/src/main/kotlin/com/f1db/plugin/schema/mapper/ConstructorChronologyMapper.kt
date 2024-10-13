package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Constructor
import com.f1db.plugin.schema.single.ConstructorChronology
import org.mapstruct.AfterMapping
import org.mapstruct.Context
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import com.f1db.plugin.schema.splitted.ConstructorChronology as SplittedConstructorChronology

/**
 * The constructor chronology mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ConstructorChronologyMapper {

    @Mapping(target = "parentConstructorId", ignore = true)
    fun toSplittedConstructorChronology(constructorChronology: ConstructorChronology, @Context parentConstructor: Constructor): SplittedConstructorChronology

    fun toSplittedConstructorChronology(constructorChronology: List<ConstructorChronology>, @Context parentConstructor: Constructor): List<SplittedConstructorChronology>

    @AfterMapping
    fun afterMapping(@MappingTarget splittedConstructorChronology: SplittedConstructorChronology, @Context parentConstructor: Constructor) {
        splittedConstructorChronology.parentConstructorId = parentConstructor.id
    }
}
