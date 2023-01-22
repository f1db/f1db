package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.GrandPrix
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.GrandPrix as SplittedGrandPrix

/**
 * The grand prix mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface GrandPrixMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(GrandPrixMapper::class.java)
    }

    fun toSplittedGrandPrix(grandPrix: GrandPrix): SplittedGrandPrix

    fun toSplittedGrandsPrix(grandsPrix: List<GrandPrix>): List<SplittedGrandPrix>
}
