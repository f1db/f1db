package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Circuit
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Circuit as SplittedCircuit

/**
 * The circuit mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface CircuitMapper {

    fun toSplittedCircuit(circuit: Circuit): SplittedCircuit

    fun toSplittedCircuits(circuits: List<Circuit>): List<SplittedCircuit>
}
