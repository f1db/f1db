package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Circuit
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Circuit as SplittedCircuit

/**
 * The circuit mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface CircuitMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(CircuitMapper::class.java)
    }

    fun toSplittedCircuit(circuit: Circuit): SplittedCircuit

    fun toSplittedCircuits(circuits: List<Circuit>): List<SplittedCircuit>
}
