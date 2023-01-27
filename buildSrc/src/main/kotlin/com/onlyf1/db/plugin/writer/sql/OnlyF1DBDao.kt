package com.onlyf1.db.plugin.writer.sql

import com.onlyf1.db.schema.splitted.Circuit
import com.onlyf1.db.schema.splitted.Constructor
import com.onlyf1.db.schema.splitted.ConstructorPreviousNextConstructor
import com.onlyf1.db.schema.splitted.Continent
import com.onlyf1.db.schema.splitted.Country
import com.onlyf1.db.schema.splitted.Driver
import com.onlyf1.db.schema.splitted.DriverFamilyRelationship
import com.onlyf1.db.schema.splitted.DriverOfTheDayResult
import com.onlyf1.db.schema.splitted.EngineManufacturer
import com.onlyf1.db.schema.splitted.Entrant
import com.onlyf1.db.schema.splitted.FastestLap
import com.onlyf1.db.schema.splitted.GrandPrix
import com.onlyf1.db.schema.splitted.PitStop
import com.onlyf1.db.schema.splitted.PracticeResult
import com.onlyf1.db.schema.splitted.QualifyingResult
import com.onlyf1.db.schema.splitted.Race
import com.onlyf1.db.schema.splitted.RaceConstructorStanding
import com.onlyf1.db.schema.splitted.RaceDriverStanding
import com.onlyf1.db.schema.splitted.RaceResult
import com.onlyf1.db.schema.splitted.Season
import com.onlyf1.db.schema.splitted.SeasonConstructorStanding
import com.onlyf1.db.schema.splitted.SeasonDriverStanding
import com.onlyf1.db.schema.splitted.SeasonEntrant
import com.onlyf1.db.schema.splitted.SeasonEntrantConstructor
import com.onlyf1.db.schema.splitted.SeasonEntrantDriver
import com.onlyf1.db.schema.splitted.SeasonEntrantTyreManufacturer
import com.onlyf1.db.schema.splitted.StartingGridPosition
import com.onlyf1.db.schema.splitted.TyreManufacturer
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.locator.UseClasspathSqlLocator
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlScript

/**
 * The OnlyF1-DB DAO.
 *
 * @author Marcel Overdijk
 */
@UseClasspathSqlLocator
interface OnlyF1DBDao {

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

    @SqlScript
    fun vacuum()

}