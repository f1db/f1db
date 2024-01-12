package com.f1db.plugin.mapper

import com.f1db.schema.single.Entrant
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Entrant as SplittedEntrant

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
