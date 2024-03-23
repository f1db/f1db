package com.f1db.plugin.reader.databind

import com.f1db.plugin.schema.single.DriverOfTheDayResult
import com.fasterxml.jackson.databind.util.StdConverter

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
