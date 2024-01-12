package com.f1db.plugin.extensions

import com.f1db.plugin.mapper.CircuitMapper
import com.f1db.plugin.mapper.ConstructorMapper
import com.f1db.plugin.mapper.ConstructorPreviousNextConstructorMapper
import com.f1db.plugin.mapper.ContinentMapper
import com.f1db.plugin.mapper.CountryMapper
import com.f1db.plugin.mapper.DriverFamilyRelationshipMapper
import com.f1db.plugin.mapper.DriverMapper
import com.f1db.plugin.mapper.DriverOfTheDayResultMapper
import com.f1db.plugin.mapper.EngineManufacturerMapper
import com.f1db.plugin.mapper.EntrantMapper
import com.f1db.plugin.mapper.FastestLapMapper
import com.f1db.plugin.mapper.GrandPrixMapper
import com.f1db.plugin.mapper.PitStopMapper
import com.f1db.plugin.mapper.PracticeResultMapper
import com.f1db.plugin.mapper.QualifyingResultMapper
import com.f1db.plugin.mapper.RaceConstructorStandingMapper
import com.f1db.plugin.mapper.RaceDriverStandingMapper
import com.f1db.plugin.mapper.RaceMapper
import com.f1db.plugin.mapper.RaceResultMapper
import com.f1db.plugin.mapper.SeasonConstructorStandingMapper
import com.f1db.plugin.mapper.SeasonDriverStandingMapper
import com.f1db.plugin.mapper.SeasonEntrantConstructorMapper
import com.f1db.plugin.mapper.SeasonEntrantDriverMapper
import com.f1db.plugin.mapper.SeasonEntrantMapper
import com.f1db.plugin.mapper.SeasonEntrantTyreManufacturerMapper
import com.f1db.plugin.mapper.SeasonMapper
import com.f1db.plugin.mapper.StartingGridPositionMapper
import com.f1db.plugin.mapper.TyreManufacturerMapper
import com.f1db.schema.single.F1db
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

val F1db.splitted: Splitted
    get() = Splitted(this)

class Splitted(private val db: F1db) {

    val drivers: List<Driver>
        get() = DriverMapper.INSTANCE.toSplittedDrivers(db.drivers)

    val driverFamilyRelationships: List<DriverFamilyRelationship>
        get() = db.drivers
            .filter { it.familyRelationships != null }
            .flatMap { driver -> DriverFamilyRelationshipMapper.INSTANCE.toSplittedDriverFamilyRelationships(driver.familyRelationships, driver) }

    val constructors: List<Constructor>
        get() = ConstructorMapper.INSTANCE.toSplittedConstructors(db.constructors)

    val constructorPreviousNextConstructors: List<ConstructorPreviousNextConstructor>
        get() = db.constructors
            .filter { it.previousNextConstructors != null }
            .flatMap { constructor ->
                ConstructorPreviousNextConstructorMapper.INSTANCE.toSplittedPreviousNextConstructors(
                    constructor.previousNextConstructors,
                    constructor
                )
            }

    val engineManufacturers: List<EngineManufacturer>
        get() = EngineManufacturerMapper.INSTANCE.toSplittedEngineManufacturers(db.engineManufacturers)

    val tyreManufacturers: List<TyreManufacturer>
        get() = TyreManufacturerMapper.INSTANCE.toSplittedTyreManufacturers(db.tyreManufacturers)

    val entrants: List<Entrant>
        get() = EntrantMapper.INSTANCE.toSplittedEntrants(db.entrants)

    val circuits: List<Circuit>
        get() = CircuitMapper.INSTANCE.toSplittedCircuits(db.circuits)

    val grandsPrix: List<GrandPrix>
        get() = GrandPrixMapper.INSTANCE.toSplittedGrandsPrix(db.grandsPrix)

    val seasons: List<Season>
        get() = SeasonMapper.INSTANCE.toSplittedSeasons(db.seasons)

    val seasonEntrants: List<SeasonEntrant>
        get() = db.seasons
            .filter { it.entrants != null }
            .flatMap { season ->
                season.entrants.map { seasonEntrant ->
                    SeasonEntrantMapper.INSTANCE.toSplittedSeasonEntrant(seasonEntrant, season)
                }
            }

    val seasonEntrantConstructors: List<SeasonEntrantConstructor>
        get() = db.seasons
            .filter { it.entrants != null }
            .flatMap { season ->
                season.entrants.flatMap { seasonEntrant ->
                    seasonEntrant.constructors.map { seasonEntrantConstructor ->
                        SeasonEntrantConstructorMapper.INSTANCE.toSplittedSeasonEntrantConstructor(seasonEntrantConstructor, season, seasonEntrant)
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
                            SeasonEntrantTyreManufacturerMapper.INSTANCE.toSplittedSeasonEntrantTyreManufacturer(
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
                            SeasonEntrantDriverMapper.INSTANCE.toSplittedSeasonEntrantDriver(
                                seasonEntrantDriver,
                                season,
                                seasonEntrant,
                                seasonEntrantConstructor
                            )
                        }
                    }
                }
            }

    val seasonDriverStandings: List<SeasonDriverStanding>
        get() = db.seasons
            .filter { it.driverStandings != null }
            .flatMap { season ->
                season.driverStandings.mapIndexed { index, driverStanding ->
                    SeasonDriverStandingMapper.INSTANCE.toSplittedSeasonDriverStanding(driverStanding, season, index + 1)
                }
            }

    val seasonConstructorStandings: List<SeasonConstructorStanding>
        get() = db.seasons
            .filter { it.constructorStandings != null }
            .flatMap { season ->
                season.constructorStandings.mapIndexed { index, constructorStanding ->
                    SeasonConstructorStandingMapper.INSTANCE.toSplittedSeasonConstructorStanding(constructorStanding, season, index + 1)
                }
            }

    val races: List<Race>
        get() = RaceMapper.INSTANCE.toSplittedRaces(db.races)

    val racePreQualifyingResults: List<QualifyingResult>
        get() = db.races
            .filter { it.preQualifyingResults != null }
            .flatMap { race ->
                race.preQualifyingResults.mapIndexed { index, preQualifyingResult ->
                    QualifyingResultMapper.INSTANCE.toSplittedQualifyingResult(preQualifyingResult, race, index + 1)
                }
            }

    val raceFreePractice1Results: List<PracticeResult>
        get() = db.races
            .filter { it.freePractice1Results != null }
            .flatMap { race ->
                race.freePractice1Results.mapIndexed { index, freePractice1Result ->
                    PracticeResultMapper.INSTANCE.toSplittedPracticeResult(freePractice1Result, race, index + 1)
                }
            }

    val raceFreePractice2Results: List<PracticeResult>
        get() = db.races
            .filter { it.freePractice2Results != null }
            .flatMap { race ->
                race.freePractice2Results.mapIndexed { index, freePractice2Result ->
                    PracticeResultMapper.INSTANCE.toSplittedPracticeResult(freePractice2Result, race, index + 1)
                }
            }

    val raceFreePractice3Results: List<PracticeResult>
        get() = db.races
            .filter { it.freePractice3Results != null }
            .flatMap { race ->
                race.freePractice3Results.mapIndexed { index, freePractice3Result ->
                    PracticeResultMapper.INSTANCE.toSplittedPracticeResult(freePractice3Result, race, index + 1)
                }
            }

    val raceFreePractice4Results: List<PracticeResult>
        get() = db.races
            .filter { it.freePractice4Results != null }
            .flatMap { race ->
                race.freePractice4Results.mapIndexed { index, freePractice4Result ->
                    PracticeResultMapper.INSTANCE.toSplittedPracticeResult(freePractice4Result, race, index + 1)
                }
            }

    val raceQualifying1Results: List<QualifyingResult>
        get() = db.races
            .filter { it.qualifying1Results != null }
            .flatMap { race ->
                race.qualifying1Results.mapIndexed { index, qualifying1Result ->
                    QualifyingResultMapper.INSTANCE.toSplittedQualifyingResult(qualifying1Result, race, index + 1)
                }
            }

    val raceQualifying2Results: List<QualifyingResult>
        get() = db.races
            .filter { it.qualifying2Results != null }
            .flatMap { race ->
                race.qualifying2Results.mapIndexed { index, qualifying2Result ->
                    QualifyingResultMapper.INSTANCE.toSplittedQualifyingResult(qualifying2Result, race, index + 1)
                }
            }

    val raceQualifyingResults: List<QualifyingResult>
        get() = db.races
            .filter { it.qualifyingResults != null }
            .flatMap { race ->
                race.qualifyingResults.mapIndexed { index, qualifyingResult ->
                    QualifyingResultMapper.INSTANCE.toSplittedQualifyingResult(qualifyingResult, race, index + 1)
                }
            }

    val raceSprintQualifyingResults: List<QualifyingResult>
        get() = db.races
            .filter { it.sprintQualifyingResults != null }
            .flatMap { race ->
                race.sprintQualifyingResults.mapIndexed { index, sprintQualifyingResult ->
                    QualifyingResultMapper.INSTANCE.toSplittedQualifyingResult(sprintQualifyingResult, race, index + 1)
                }
            }

    val raceSprintStartingGridPositions: List<StartingGridPosition>
        get() = db.races
            .filter { it.sprintStartingGridPositions != null }
            .flatMap { race ->
                race.sprintStartingGridPositions.mapIndexed { index, sprintStartingGridPosition ->
                    StartingGridPositionMapper.INSTANCE.toSplittedStartingGridPosition(sprintStartingGridPosition, race, index + 1)
                }
            }

    val raceSprintRaceResults: List<RaceResult>
        get() = db.races
            .filter { it.sprintRaceResults != null }
            .flatMap { race ->
                race.sprintRaceResults.mapIndexed { index, sprintRaceResult ->
                    RaceResultMapper.INSTANCE.toSplittedRaceResult(sprintRaceResult, race, index + 1)
                }
            }

    val raceWarmingUpResults: List<PracticeResult>
        get() = db.races
            .filter { it.warmingUpResults != null }
            .flatMap { race ->
                race.warmingUpResults.mapIndexed { index, warmingUpResult ->
                    PracticeResultMapper.INSTANCE.toSplittedPracticeResult(warmingUpResult, race, index + 1)
                }
            }

    val raceStartingGridPositions: List<StartingGridPosition>
        get() = db.races
            .filter { it.startingGridPositions != null }
            .flatMap { race ->
                race.startingGridPositions.mapIndexed { index, startingGridPosition ->
                    StartingGridPositionMapper.INSTANCE.toSplittedStartingGridPosition(startingGridPosition, race, index + 1)
                }
            }

    val raceRaceResults: List<RaceResult>
        get() = db.races
            .filter { it.raceResults != null }
            .flatMap { race ->
                race.raceResults.mapIndexed { index, raceResult ->
                    RaceResultMapper.INSTANCE.toSplittedRaceResult(raceResult, race, index + 1)
                }
            }

    val raceFastestLaps: List<FastestLap>
        get() = db.races
            .filter { it.fastestLaps != null }
            .flatMap { race ->
                race.fastestLaps.mapIndexed { index, fastestLap ->
                    FastestLapMapper.INSTANCE.toSplittedFastestLap(fastestLap, race, index + 1)
                }
            }

    val racePitStops: List<PitStop>
        get() = db.races
            .filter { it.pitStops != null }
            .flatMap { race ->
                race.pitStops.mapIndexed { index, pitStop ->
                    PitStopMapper.INSTANCE.toSplittedPitStop(pitStop, race, index + 1)
                }
            }

    val raceDriverOfTheDayResults: List<DriverOfTheDayResult>
        get() = db.races
            .filter { it.driverOfTheDayResults != null }
            .flatMap { race ->
                race.driverOfTheDayResults.mapIndexed { index, driverOfTheDayResult ->
                    DriverOfTheDayResultMapper.INSTANCE.toSplittedDriverOfTheDayResult(driverOfTheDayResult, race, index + 1)
                }
            }

    val raceDriverStandings: List<RaceDriverStanding>
        get() = db.races
            .filter { it.driverStandings != null }
            .flatMap { race ->
                race.driverStandings.mapIndexed { index, driverStanding ->
                    RaceDriverStandingMapper.INSTANCE.toSplittedRaceDriverStanding(driverStanding, race, index + 1)
                }
            }

    val raceConstructorStandings: List<RaceConstructorStanding>
        get() = db.races
            .filter { it.constructorStandings != null }
            .flatMap { race ->
                race.constructorStandings.mapIndexed { index, constructorStanding ->
                    RaceConstructorStandingMapper.INSTANCE.toSplittedRaceConstructorStanding(constructorStanding, race, index + 1)
                }
            }

    val continents: List<Continent>
        get() = ContinentMapper.INSTANCE.toSplittedContinents(db.continents)

    val countries: List<Country>
        get() = CountryMapper.INSTANCE.toSplittedCountries(db.countries)
}
