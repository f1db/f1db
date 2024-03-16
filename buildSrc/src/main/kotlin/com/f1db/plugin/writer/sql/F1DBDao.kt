package com.f1db.plugin.writer.sql

import com.f1db.schema.splitted.Circuit
import com.f1db.schema.splitted.Constructor
import com.f1db.schema.splitted.ConstructorPreviousNextConstructor
import com.f1db.schema.splitted.Continent
import com.f1db.schema.splitted.Country
import com.f1db.schema.splitted.Driver
import com.f1db.schema.splitted.DriverFamilyRelationship
import com.f1db.schema.splitted.DriverOfTheDayResult
import com.f1db.schema.splitted.EngineManufacturer
import com.f1db.schema.splitted.Entrant
import com.f1db.schema.splitted.FastestLap
import com.f1db.schema.splitted.GrandPrix
import com.f1db.schema.splitted.PitStop
import com.f1db.schema.splitted.PracticeResult
import com.f1db.schema.splitted.QualifyingResult
import com.f1db.schema.splitted.Race
import com.f1db.schema.splitted.RaceConstructorStanding
import com.f1db.schema.splitted.RaceDriverStanding
import com.f1db.schema.splitted.RaceResult
import com.f1db.schema.splitted.Season
import com.f1db.schema.splitted.SeasonConstructorStanding
import com.f1db.schema.splitted.SeasonDriverStanding
import com.f1db.schema.splitted.SeasonEntrant
import com.f1db.schema.splitted.SeasonEntrantConstructor
import com.f1db.schema.splitted.SeasonEntrantDriver
import com.f1db.schema.splitted.SeasonEntrantTyreManufacturer
import com.f1db.schema.splitted.StartingGridPosition
import com.f1db.schema.splitted.TyreManufacturer
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlScript

/**
 * The F1DB DAO.
 *
 * @author Marcel Overdijk
 */
@UseClasspathSqlLocator
interface F1DBDao {

    @SqlScript("create_schema")
    fun createSchema()

    @SqlBatch("insert_continent")
    fun insertContinents(@BindBean continents: List<Continent>)

    @SqlBatch("insert_country")
    fun insertCountries(@BindBean countries: List<Country>)

    @SqlBatch("insert_driver")
    fun insertDrivers(@BindBean drivers: List<Driver>)

    @SqlBatch("insert_driver_family_relationship")
    fun insertDriverFamilyRelationships(@BindBean driverFamilyRelationships: List<DriverFamilyRelationship>)

    @SqlBatch("insert_constructor")
    fun insertConstructors(@BindBean constructors: List<Constructor>)

    @SqlBatch("insert_constructor_previous_next_constructor")
    fun insertConstructorPreviousNextConstructors(@BindBean constructorPreviousNextConstructors: List<ConstructorPreviousNextConstructor>)

    @SqlBatch("insert_engine_manufacturer")
    fun insertEngineManufacturers(@BindBean engineManufacturers: List<EngineManufacturer>)

    @SqlBatch("insert_tyre_manufacturer")
    fun insertTyreManufacturers(@BindBean tyreManufacturers: List<TyreManufacturer>)

    @SqlBatch("insert_entrant")
    fun insertEntrants(@BindBean entrants: List<Entrant>)

    @SqlBatch("insert_circuit")
    fun insertCircuits(@BindBean circuits: List<Circuit>, @Bind("previousNamesAsString") previousNames: List<String?>)

    @SqlBatch("insert_grand_prix")
    fun insertGrandsPrix(@BindBean grandsPrix: List<GrandPrix>)

    @SqlBatch("insert_season")
    fun insertSeasons(@BindBean seasons: List<Season>)

    @SqlBatch("insert_season_entrant")
    fun insertSeasonEntrants(@BindBean seasonEntrants: List<SeasonEntrant>)

    @SqlBatch("insert_season_entrant_constructor")
    fun insertSeasonEntrantConstructors(@BindBean seasonEntrantConstructors: List<SeasonEntrantConstructor>)

    @SqlBatch("insert_season_entrant_tyre_manufacturer")
    fun insertSeasonEntrantTyreManufacturers(@BindBean seasonEntrantTyreManufacturers: List<SeasonEntrantTyreManufacturer>)

    @SqlBatch("insert_season_entrant_driver")
    fun insertSeasonEntrantDrivers(@BindBean seasonEntrantDrivers: List<SeasonEntrantDriver>, @Bind("roundsAsString") previousNames: List<String?>)

    @SqlBatch("insert_season_driver_standing")
    fun insertSeasonDriverStandings(@BindBean seasonDriverStandings: List<SeasonDriverStanding>)

    @SqlBatch("insert_season_constructor_standing")
    fun insertSeasonConstructorStandings(@BindBean seasonConstructorStandings: List<SeasonConstructorStanding>)

    @SqlBatch("insert_race")
    fun insertRaces(@BindBean races: List<Race>)

    @SqlBatch("insert_race_practice_result")
    fun insertRacePracticeResult(@BindBean racePracticeResults: List<PracticeResult>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_qualifying_result")
    fun insertRaceQualifyingResult(@BindBean raceQualifyingResults: List<QualifyingResult>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_starting_grid_position")
    fun insertRaceStartingGridPosition(@BindBean raceStartingGridPosition: List<StartingGridPosition>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_race_result")
    fun insertRaceRaceResult(@BindBean raceRaceResult: List<RaceResult>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_fastest_lap")
    fun insertRaceFastestLap(@BindBean raceFastestLap: List<FastestLap>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_pit_stop")
    fun insertRacePitStop(@BindBean racePitStop: List<PitStop>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_driver_of_the_day_result")
    fun insertRaceDriverOfTheDayResult(@BindBean raceDriverOfTheDayResult: List<DriverOfTheDayResult>, @Bind("raceDataType") type: String)

    @SqlBatch("insert_race_driver_standing")
    fun insertRaceDriverStandings(@BindBean raceDriverStandings: List<RaceDriverStanding>)

    @SqlBatch("insert_race_constructor_standing")
    fun insertRaceConstructorStandings(@BindBean raceConstructorStandings: List<RaceConstructorStanding>)

}