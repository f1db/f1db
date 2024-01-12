package com.f1db.plugin.mapper

import com.f1db.schema.single.Driver
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.f1db.schema.splitted.Driver as SplittedDriver

/**
 * The driver mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper
interface DriverMapper {

    companion object {
        val INSTANCE = Mappers.getMapper(DriverMapper::class.java)
    }

    fun toSplittedDriver(driver: Driver): SplittedDriver

    fun toSplittedDrivers(drivers: List<Driver>): List<SplittedDriver>
}
