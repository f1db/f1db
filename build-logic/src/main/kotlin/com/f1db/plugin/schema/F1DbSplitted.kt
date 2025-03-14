package com.f1db.plugin.schema

import com.f1db.plugin.schema.mapper.chassisMapper
import com.f1db.plugin.schema.mapper.circuitMapper
import com.f1db.plugin.schema.mapper.constructorChronologyMapper
import com.f1db.plugin.schema.mapper.constructorMapper
import com.f1db.plugin.schema.mapper.continentMapper
import com.f1db.plugin.schema.mapper.countryMapper
import com.f1db.plugin.schema.mapper.driverFamilyRelationshipMapper
import com.f1db.plugin.schema.mapper.driverMapper
import com.f1db.plugin.schema.mapper.engineManufacturerMapper
import com.f1db.plugin.schema.mapper.engineMapper
import com.f1db.plugin.schema.mapper.entrantMapper
import com.f1db.plugin.schema.mapper.grandPrixMapper
import com.f1db.plugin.schema.mapper.raceConstructorStandingMapper
import com.f1db.plugin.schema.mapper.raceDriverOfTheDayResultMapper
import com.f1db.plugin.schema.mapper.raceDriverStandingMapper
import com.f1db.plugin.schema.mapper.raceFastestLapMapper
import com.f1db.plugin.schema.mapper.raceMapper
import com.f1db.plugin.schema.mapper.racePitStopMapper
import com.f1db.plugin.schema.mapper.racePracticeResultMapper
import com.f1db.plugin.schema.mapper.raceQualifyingResultMapper
import com.f1db.plugin.schema.mapper.raceRaceResultMapper
import com.f1db.plugin.schema.mapper.raceStartingGridPositionMapper
import com.f1db.plugin.schema.mapper.seasonConstructorMapper
import com.f1db.plugin.schema.mapper.seasonConstructorStandingMapper
import com.f1db.plugin.schema.mapper.seasonDriverMapper
import com.f1db.plugin.schema.mapper.seasonDriverStandingMapper
import com.f1db.plugin.schema.mapper.seasonEngineManufacturerMapper
import com.f1db.plugin.schema.mapper.seasonEntrantChassisMapper
import com.f1db.plugin.schema.mapper.seasonEntrantConstructorMapper
import com.f1db.plugin.schema.mapper.seasonEntrantDriverMapper
import com.f1db.plugin.schema.mapper.seasonEntrantEngineMapper
import com.f1db.plugin.schema.mapper.seasonEntrantMapper
import com.f1db.plugin.schema.mapper.seasonEntrantTyreManufacturerMapper
import com.f1db.plugin.schema.mapper.seasonMapper
import com.f1db.plugin.schema.mapper.seasonTyreManufacturerMapper
import com.f1db.plugin.schema.mapper.tyreManufacturerMapper
import com.f1db.plugin.schema.single.F1db
import com.f1db.plugin.schema.splitted.Chassis
import com.f1db.plugin.schema.splitted.Circuit
import com.f1db.plugin.schema.splitted.Constructor
import com.f1db.plugin.schema.splitted.ConstructorChronology
import com.f1db.plugin.schema.splitted.Continent
import com.f1db.plugin.schema.splitted.Country
import com.f1db.plugin.schema.splitted.Driver
import com.f1db.plugin.schema.splitted.DriverFamilyRelationship
import com.f1db.plugin.schema.splitted.DriverOfTheDayResult
import com.f1db.plugin.schema.splitted.Engine
import com.f1db.plugin.schema.splitted.EngineManufacturer
import com.f1db.plugin.schema.splitted.Entrant
import com.f1db.plugin.schema.splitted.FastestLap
import com.f1db.plugin.schema.splitted.GrandPrix
import com.f1db.plugin.schema.splitted.PitStop
import com.f1db.plugin.schema.splitted.PracticeResult
import com.f1db.plugin.schema.splitted.QualifyingResult
import com.f1db.plugin.schema.splitted.Race
import com.f1db.plugin.schema.splitted.RaceConstructorStanding
import com.f1db.plugin.schema.splitted.RaceDriverStanding
import com.f1db.plugin.schema.splitted.RaceResult
import com.f1db.plugin.schema.splitted.Season
import com.f1db.plugin.schema.splitted.SeasonConstructor
import com.f1db.plugin.schema.splitted.SeasonConstructorStanding
import com.f1db.plugin.schema.splitted.SeasonDriver
import com.f1db.plugin.schema.splitted.SeasonDriverStanding
import com.f1db.plugin.schema.splitted.SeasonEngineManufacturer
import com.f1db.plugin.schema.splitted.SeasonEntrant
import com.f1db.plugin.schema.splitted.SeasonEntrantChassis
import com.f1db.plugin.schema.splitted.SeasonEntrantConstructor
import com.f1db.plugin.schema.splitted.SeasonEntrantDriver
import com.f1db.plugin.schema.splitted.SeasonEntrantEngine
import com.f1db.plugin.schema.splitted.SeasonEntrantTyreManufacturer
import com.f1db.plugin.schema.splitted.SeasonTyreManufacturer
import com.f1db.plugin.schema.splitted.StartingGridPosition
import com.f1db.plugin.schema.splitted.TyreManufacturer

/**
 * The splitted F1DB data.
 *
 * @author Marcel Overdijk
 */
class F1DbSplitted(private val db: F1db) {

    val drivers: List<Driver>
        get() = driverMapper.toSplittedDrivers(db.drivers)

    val driverFamilyRelationships: List<DriverFamilyRelationship>
        get() = db.drivers
                .filter { it.familyRelationships != null }
                .flatMap { driver -> driverFamilyRelationshipMapper.toSplittedDriverFamilyRelationships(driver.familyRelationships, driver) }

    val constructors: List<Constructor>
        get() = constructorMapper.toSplittedConstructors(db.constructors)

    val constructorChronology: List<ConstructorChronology>
        get() = db.constructors
                .filter { it.chronology != null }
                .flatMap { constructor -> constructorChronologyMapper.toSplittedConstructorChronology(constructor.chronology, constructor) }

    val chassis: List<Chassis>
        get() = chassisMapper.toSplittedChassis(db.chassis)

    val engineManufacturers: List<EngineManufacturer>
        get() = engineManufacturerMapper.toSplittedEngineManufacturers(db.engineManufacturers)

    val engines: List<Engine>
        get() = engineMapper.toSplittedEngines(db.engines)

    val tyreManufacturers: List<TyreManufacturer>
        get() = tyreManufacturerMapper.toSplittedTyreManufacturers(db.tyreManufacturers)

    val entrants: List<Entrant>
        get() = entrantMapper.toSplittedEntrants(db.entrants)

    val circuits: List<Circuit>
        get() = circuitMapper.toSplittedCircuits(db.circuits)

    val grandsPrix: List<GrandPrix>
        get() = grandPrixMapper.toSplittedGrandsPrix(db.grandsPrix)

    val seasons: List<Season>
        get() = seasonMapper.toSplittedSeasons(db.seasons)

    val seasonEntrants: List<SeasonEntrant>
        get() = db.seasons
                .filter { it.entrants != null }
                .flatMap { season ->
                    season.entrants.map { seasonEntrant ->
                        seasonEntrantMapper.toSplittedSeasonEntrant(seasonEntrant, season)
                    }
                }

    val seasonEntrantConstructors: List<SeasonEntrantConstructor>
        get() = db.seasons
                .filter { it.entrants != null }
                .flatMap { season ->
                    season.entrants.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.map { seasonEntrantConstructor ->
                            seasonEntrantConstructorMapper.toSplittedSeasonEntrantConstructor(seasonEntrantConstructor, season, seasonEntrant)
                        }
                    }
                }

    val seasonEntrantChassis: List<SeasonEntrantChassis>
        get() = db.seasons
                .filter { it.entrants != null }
                .flatMap { season ->
                    season.entrants.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.flatMap { seasonEntrantConstructor ->
                            seasonEntrantConstructor.chassis.map { seasonEntrantChassis ->
                                seasonEntrantChassisMapper.toSplittedSeasonEntrantChassis(
                                        seasonEntrantChassis,
                                        season,
                                        seasonEntrant,
                                        seasonEntrantConstructor
                                )
                            }
                        }
                    }
                }

    val seasonEntrantEngines: List<SeasonEntrantEngine>
        get() = db.seasons
                .filter { it.entrants != null }
                .flatMap { season ->
                    season.entrants.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.flatMap { seasonEntrantConstructor ->
                            seasonEntrantConstructor.engines.map { seasonEntrantEngine ->
                                seasonEntrantEngineMapper.toSplittedSeasonEntrantEngine(
                                        seasonEntrantEngine,
                                        season,
                                        seasonEntrant,
                                        seasonEntrantConstructor
                                )
                            }
                        }
                    }
                }

    val seasonEntrantTyreManufacturers: List<SeasonEntrantTyreManufacturer>
        get() = db.seasons
                .filter { it.entrants != null }
                .flatMap { season ->
                    season.entrants.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.flatMap { seasonEntrantConstructor ->
                            seasonEntrantConstructor.tyreManufacturers.map { seasonEntrantTyreManufacturer ->
                                seasonEntrantTyreManufacturerMapper.toSplittedSeasonEntrantTyreManufacturer(
                                        seasonEntrantTyreManufacturer,
                                        season,
                                        seasonEntrant,
                                        seasonEntrantConstructor
                                )
                            }
                        }
                    }
                }

    val seasonEntrantDrivers: List<SeasonEntrantDriver>
        get() = db.seasons
                .filter { it.entrants != null }
                .flatMap { season ->
                    season.entrants.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.flatMap { seasonEntrantConstructor ->
                            seasonEntrantConstructor.drivers.map { seasonEntrantDriver ->
                                seasonEntrantDriverMapper.toSplittedSeasonEntrantDriver(
                                        seasonEntrantDriver,
                                        season,
                                        seasonEntrant,
                                        seasonEntrantConstructor
                                )
                            }
                        }
                    }
                }

    val seasonConstructors: List<SeasonConstructor>
        get() = db.seasons
                .filter { it.constructors != null }
                .flatMap { season ->
                    season.constructors.map { seasonConstructor ->
                        seasonConstructorMapper.toSplittedSeasonConstructor(seasonConstructor, season)
                    }
                }

    val seasonEngineManufacturers: List<SeasonEngineManufacturer>
        get() = db.seasons
                .filter { it.engineManufacturers != null }
                .flatMap { season ->
                    season.engineManufacturers.map { seasonEngineManufacturer ->
                        seasonEngineManufacturerMapper.toSplittedSeasonEngineManufacturer(seasonEngineManufacturer, season)
                    }
                }

    val seasonTyreManufacturers: List<SeasonTyreManufacturer>
        get() = db.seasons
                .filter { it.tyreManufacturers != null }
                .flatMap { season ->
                    season.tyreManufacturers.map { seasonTyreManufacturer ->
                        seasonTyreManufacturerMapper.toSplittedSeasonTyreManufacturer(seasonTyreManufacturer, season)
                    }
                }

    val seasonDrivers: List<SeasonDriver>
        get() = db.seasons
                .filter { it.drivers != null }
                .flatMap { season ->
                    season.drivers.map { seasonDriver ->
                        seasonDriverMapper.toSplittedSeasonDriver(seasonDriver, season)
                    }
                }

    val seasonDriverStandings: List<SeasonDriverStanding>
        get() = db.seasons
                .filter { it.driverStandings != null }
                .flatMap { season -> seasonDriverStandingMapper.toSplittedSeasonDriverStandings(season.driverStandings, season) }

    val seasonConstructorStandings: List<SeasonConstructorStanding>
        get() = db.seasons
                .filter { it.constructorStandings != null }
                .flatMap { season -> seasonConstructorStandingMapper.toSplittedSeasonConstructorStandings(season.constructorStandings, season) }

    val races: List<Race>
        get() = raceMapper.toSplittedRaces(db.races)

    val racePreQualifyingResults: List<QualifyingResult>
        get() = db.races
                .filter { it.preQualifyingResults != null }
                .flatMap { race -> raceQualifyingResultMapper.toSplittedQualifyingResults(race.preQualifyingResults, race) }

    val raceFreePractice1Results: List<PracticeResult>
        get() = db.races
                .filter { it.freePractice1Results != null }
                .flatMap { race -> racePracticeResultMapper.toSplittedPracticeResults(race.freePractice1Results, race) }

    val raceFreePractice2Results: List<PracticeResult>
        get() = db.races
                .filter { it.freePractice2Results != null }
                .flatMap { race -> racePracticeResultMapper.toSplittedPracticeResults(race.freePractice2Results, race) }

    val raceFreePractice3Results: List<PracticeResult>
        get() = db.races
                .filter { it.freePractice3Results != null }
                .flatMap { race -> racePracticeResultMapper.toSplittedPracticeResults(race.freePractice3Results, race) }

    val raceFreePractice4Results: List<PracticeResult>
        get() = db.races
                .filter { it.freePractice4Results != null }
                .flatMap { race -> racePracticeResultMapper.toSplittedPracticeResults(race.freePractice4Results, race) }

    val raceQualifying1Results: List<QualifyingResult>
        get() = db.races
                .filter { it.qualifying1Results != null }
                .flatMap { race -> raceQualifyingResultMapper.toSplittedQualifyingResults(race.qualifying1Results, race) }

    val raceQualifying2Results: List<QualifyingResult>
        get() = db.races
                .filter { it.qualifying2Results != null }
                .flatMap { race -> raceQualifyingResultMapper.toSplittedQualifyingResults(race.qualifying2Results, race) }

    val raceQualifyingResults: List<QualifyingResult>
        get() = db.races
                .filter { it.qualifyingResults != null }
                .flatMap { race -> raceQualifyingResultMapper.toSplittedQualifyingResults(race.qualifyingResults, race) }

    val raceSprintQualifyingResults: List<QualifyingResult>
        get() = db.races
                .filter { it.sprintQualifyingResults != null }
                .flatMap { race -> raceQualifyingResultMapper.toSplittedQualifyingResults(race.sprintQualifyingResults, race) }

    val raceSprintStartingGridPositions: List<StartingGridPosition>
        get() = db.races
                .filter { it.sprintStartingGridPositions != null }
                .flatMap { race -> raceStartingGridPositionMapper.toSplittedStartingGridPositions(race.sprintStartingGridPositions, race) }

    val raceSprintRaceResults: List<RaceResult>
        get() = db.races
                .filter { it.sprintRaceResults != null }
                .flatMap { race -> raceRaceResultMapper.toSplittedRaceResults(race.sprintRaceResults, race) }

    val raceWarmingUpResults: List<PracticeResult>
        get() = db.races
                .filter { it.warmingUpResults != null }
                .flatMap { race -> racePracticeResultMapper.toSplittedPracticeResults(race.warmingUpResults, race) }

    val raceStartingGridPositions: List<StartingGridPosition>
        get() = db.races
                .filter { it.startingGridPositions != null }
                .flatMap { race -> raceStartingGridPositionMapper.toSplittedStartingGridPositions(race.startingGridPositions, race) }

    val raceRaceResults: List<RaceResult>
        get() = db.races
                .filter { it.raceResults != null }
                .flatMap { race -> raceRaceResultMapper.toSplittedRaceResults(race.raceResults, race) }

    val raceFastestLaps: List<FastestLap>
        get() = db.races
                .filter { it.fastestLaps != null }
                .flatMap { race -> raceFastestLapMapper.toSplittedFastestLaps(race.fastestLaps, race) }

    val racePitStops: List<PitStop>
        get() = db.races
                .filter { it.pitStops != null }
                .flatMap { race -> racePitStopMapper.toSplittedPitStops(race.pitStops, race) }

    val raceDriverOfTheDayResults: List<DriverOfTheDayResult>
        get() = db.races
                .filter { it.driverOfTheDayResults != null }
                .flatMap { race -> raceDriverOfTheDayResultMapper.toSplittedDriverOfTheDayResults(race.driverOfTheDayResults, race) }

    val raceDriverStandings: List<RaceDriverStanding>
        get() = db.races
                .filter { it.driverStandings != null }
                .flatMap { race -> raceDriverStandingMapper.toSplittedRaceDriverStandings(race.driverStandings, race) }

    val raceConstructorStandings: List<RaceConstructorStanding>
        get() = db.races
                .filter { it.constructorStandings != null }
                .flatMap { race -> raceConstructorStandingMapper.toSplittedRaceConstructorStandings(race.constructorStandings, race) }

    val continents: List<Continent>
        get() = continentMapper.toSplittedContinents(db.continents)

    val countries: List<Country>
        get() = countryMapper.toSplittedCountries(db.countries)
}
