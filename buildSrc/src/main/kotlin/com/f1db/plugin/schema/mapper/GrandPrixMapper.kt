package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.GrandPrix
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.GrandPrix as SplittedGrandPrix

/**
 * The grand prix mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface GrandPrixMapper {

    fun toSplittedGrandPrix(grandPrix: GrandPrix): SplittedGrandPrix

    fun toSplittedGrandsPrix(grandsPrix: List<GrandPrix>): List<SplittedGrandPrix>
}
