package com.f1db.plugin.schema.mapper

import com.f1db.plugin.schema.single.Driver
import org.mapstruct.Mapper
import com.f1db.plugin.schema.splitted.Driver as SplittedDriver

/**
 * The driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface DriverMapper {

    fun toSplittedDriver(driver: Driver): SplittedDriver

    fun toSplittedDrivers(drivers: List<Driver>): List<SplittedDriver>
}
