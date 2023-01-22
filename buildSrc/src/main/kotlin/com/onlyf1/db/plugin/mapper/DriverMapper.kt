package com.onlyf1.db.plugin.mapper

import com.onlyf1.db.schema.single.Driver
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
import com.onlyf1.db.schema.splitted.Driver as SplittedDriver

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
