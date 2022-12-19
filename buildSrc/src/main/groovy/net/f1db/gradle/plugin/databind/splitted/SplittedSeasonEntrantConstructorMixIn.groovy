package net.f1db.gradle.plugin.databind.splitted

import com.fasterxml.jackson.annotation.JsonIgnore
import net.f1db.SeasonEntrantDriver
import net.f1db.SeasonEntrantTyreManufacturer

/**
 * The splitted season entrant constructor mix-in.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedSeasonEntrantConstructorMixIn {

    @JsonIgnore
    List<SeasonEntrantTyreManufacturer> tyreManufacturers

    @JsonIgnore
    List<SeasonEntrantDriver> drivers

    @JsonIgnore
    abstract List<SeasonEntrantTyreManufacturer> getTyreManufacturers()

    @JsonIgnore
    abstract List<SeasonEntrantDriver> getDrivers()
}
