package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Chassis
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Chassis as SplittedChassis

/**
 * The chassis mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface ChassisMapper {

    fun toSplittedChassis(chassis: Chassis): SplittedChassis

    fun toSplittedChassis(chassis: List<Chassis>): List<SplittedChassis>
}
