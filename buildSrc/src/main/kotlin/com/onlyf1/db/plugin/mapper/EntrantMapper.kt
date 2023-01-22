package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Entrant
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Entrant as SplittedEntrant

/**
 * The entrant mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface EntrantMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(EntrantMapper::class.java)
    }

    fun toSplittedEntrant(entrant: Entrant): SplittedEntrant

    fun toSplittedEntrants(entrants: List<Entrant>): List<SplittedEntrant>
}
