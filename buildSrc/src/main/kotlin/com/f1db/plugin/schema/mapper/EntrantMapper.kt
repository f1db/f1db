package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Entrant
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Entrant as SplittedEntrant

/**
 * The entrant mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface EntrantMapper {

    fun toSplittedEntrant(entrant: Entrant): SplittedEntrant

    fun toSplittedEntrants(entrants: List<Entrant>): List<SplittedEntrant>
}
