package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.schema.single.DriverOfTheDayResult

/**
 * The driver of the day converter.
 *
 * @author Marcel Overdijk
 */
class DriverOfTheDayResultConverter : StdConverter<DriverOfTheDayResult, DriverOfTheDayResult>() {

    override fun convert(driverOfTheDayResult: DriverOfTheDayResult): DriverOfTheDayResult {
        driverOfTheDayResult.positionNumber = driverOfTheDayResult.positionText.toIntOrNull()
        return driverOfTheDayResult
    }
}
