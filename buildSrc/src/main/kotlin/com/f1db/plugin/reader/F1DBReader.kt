package com.f1db.plugin.reader

import com.f1db.plugin.CurrentSeason
import com.f1db.plugin.reader.databind.DriverOfTheDayResultMixIn
import com.f1db.plugin.reader.databind.FastestLapMixIn
import com.f1db.plugin.reader.databind.PitStopMixIn
import com.f1db.plugin.reader.databind.PracticeResultMixIn
import com.f1db.plugin.reader.databind.QualifyingResultMixIn
import com.f1db.plugin.reader.databind.RaceConstructorStandingMixIn
import com.f1db.plugin.reader.databind.RaceDriverStandingMixIn
import com.f1db.plugin.reader.databind.RaceResultMixIn
import com.f1db.plugin.reader.databind.SeasonConstructorStandingMixIn
import com.f1db.plugin.reader.databind.SeasonDriverStandingMixIn
import com.f1db.plugin.reader.databind.SeasonEntrantConstructorDeserializer
import com.f1db.plugin.reader.databind.SeasonEntrantDeserializer
import com.f1db.plugin.reader.databind.SeasonEntrantDriverMixIn
import com.f1db.plugin.reader.databind.StartingGridPositionMixIn
import com.f1db.plugin.schema.single.DriverOfTheDayResult
import com.f1db.plugin.schema.single.F1db
import com.f1db.plugin.schema.single.FastestLap
import com.f1db.plugin.schema.single.PitStop
import com.f1db.plugin.schema.single.PracticeResult
import com.f1db.plugin.schema.single.QualifyingResult
import com.f1db.plugin.schema.single.Race
import com.f1db.plugin.schema.single.RaceConstructorStanding
import com.f1db.plugin.schema.single.RaceDriverStanding
import com.f1db.plugin.schema.single.RaceResult
import com.f1db.plugin.schema.single.Season
import com.f1db.plugin.schema.single.SeasonConstructor
import com.f1db.plugin.schema.single.SeasonConstructorStanding
import com.f1db.plugin.schema.single.SeasonDriver
import com.f1db.plugin.schema.single.SeasonDriverStanding
import com.f1db.plugin.schema.single.SeasonEngineManufacturer
import com.f1db.plugin.schema.single.SeasonEntrant
import com.f1db.plugin.schema.single.SeasonEntrantConstructor
import com.f1db.plugin.schema.single.SeasonEntrantDriver
import com.f1db.plugin.schema.single.SeasonTyreManufacturer
import com.f1db.plugin.schema.single.StartingGridPosition
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate
import kotlin.math.min

/**
 * The F1DB (raw data) reader.
 *
 * @author Marcel Overdijk
 */
class F1DBReader(
        private val sourceDir: File,
        private val currentSeason: CurrentSeason,
) {

    private val mapper = createMapper()

    fun read(): F1db {

        println("Reading raw data....")

        val db = F1db()

        // Read simple types.

        db.continents = readValues("continents")
        db.countries = readValues("countries")
        db.drivers = readValues("drivers")
        db.constructors = readValues("constructors")
        db.chassis = readValues("chassis")
        db.engineManufacturers = readValues("engine-manufacturers")
        db.engines = readValues("engines")
        db.tyreManufacturers = readValues("tyre-manufacturers")
        db.entrants = readValues("entrants")
        db.circuits = readValues("circuits")
        db.grandsPrix = readValues("grands-prix")
        db.seasons = mutableListOf()
        db.races = mutableListOf()

        // Read seasons.

        File(sourceDir, "seasons").listFiles()?.sortedArray()?.forEach { seasonDir ->

            val year = seasonDir.name.toInt()

            // Read season.

            val season = Season()
            season.year = year
            season.entrants = readValues(seasonDir, "entrants.yml")
            season.driverStandings = readValues(seasonDir, "driver-standings.yml")
            season.constructorStandings = readValues(seasonDir, "constructor-standings.yml")

            db.seasons.add(season)

            // Read races.

            File(seasonDir, "races").listFiles()?.sortedArray()?.forEach { raceDir ->

                // Read race.

                val race: Race = readValue(raceDir, "race.yml")
                race.year = year
                race.preQualifyingResults = readValues(raceDir, "pre-qualifying-results.yml")
                race.freePractice1Results = readValues(raceDir, "free-practice-1-results.yml")
                race.freePractice2Results = readValues(raceDir, "free-practice-2-results.yml")
                race.freePractice3Results = readValues(raceDir, "free-practice-3-results.yml")
                race.freePractice4Results = readValues(raceDir, "free-practice-4-results.yml")
                race.qualifying1Results = readValues(raceDir, "qualifying-1-results.yml")
                race.qualifying2Results = readValues(raceDir, "qualifying-2-results.yml")
                race.qualifyingResults = readValues(raceDir, "qualifying-results.yml")
                race.sprintQualifyingResults = readValues(raceDir, "sprint-qualifying-results.yml")
                race.sprintStartingGridPositions = readValues(raceDir, "sprint-starting-grid-positions.yml")
                race.sprintRaceResults = readValues(raceDir, "sprint-race-results.yml")
                race.warmingUpResults = readValues(raceDir, "warming-up-results.yml")
                race.startingGridPositions = readValues(raceDir, "starting-grid-positions.yml")
                race.raceResults = readValues(raceDir, "race-results.yml")
                race.fastestLaps = readValues(raceDir, "fastest-laps.yml")
                race.pitStops = readValues(raceDir, "pit-stops.yml")
                race.driverOfTheDayResults = readValues(raceDir, "driver-of-the-day-results.yml")
                race.driverStandings = readValues(raceDir, "driver-standings.yml")
                race.constructorStandings = readValues(raceDir, "constructor-standings.yml")

                db.races.add(race)
            }
        }

        println("Enriching data......")

        db.drivers.forEach { driver ->
            driver.familyRelationships?.forEachIndexed { index, familyRelationship ->
                familyRelationship.positionDisplayOrder = index + 1
            }
            driver.totalChampionshipWins = 0
            driver.totalRaceEntries = 0
            driver.totalRaceStarts = 0
            driver.totalRaceWins = 0
            driver.totalRaceLaps = 0
            driver.totalPodiums = 0
            driver.totalPoints = 0.toBigDecimal()
            driver.totalChampionshipPoints = 0.toBigDecimal()
            driver.totalPolePositions = 0
            driver.totalFastestLaps = 0
            driver.totalDriverOfTheDay = 0
            driver.totalGrandSlams = 0
        }

        db.constructors.forEach { constructor ->
            constructor.chronology?.forEachIndexed { index, chronology ->
                chronology.positionDisplayOrder = index + 1
            }
            constructor.totalChampionshipWins = 0
            constructor.totalRaceEntries = 0
            constructor.totalRaceStarts = 0
            constructor.totalRaceWins = 0
            constructor.total1And2Finishes = 0
            constructor.totalRaceLaps = 0
            constructor.totalPodiums = 0
            constructor.totalPodiumRaces = 0
            constructor.totalPoints = 0.toBigDecimal()
            constructor.totalChampionshipPoints = 0.toBigDecimal()
            constructor.totalPolePositions = 0
            constructor.totalFastestLaps = 0
        }

        db.engineManufacturers.forEach { engineManufacturer ->
            engineManufacturer.totalChampionshipWins = 0
            engineManufacturer.totalRaceEntries = 0
            engineManufacturer.totalRaceStarts = 0
            engineManufacturer.totalRaceWins = 0
            engineManufacturer.totalRaceLaps = 0
            engineManufacturer.totalPodiums = 0
            engineManufacturer.totalPodiumRaces = 0
            engineManufacturer.totalPoints = 0.toBigDecimal()
            engineManufacturer.totalChampionshipPoints = 0.toBigDecimal()
            engineManufacturer.totalPolePositions = 0
            engineManufacturer.totalFastestLaps = 0
        }

        db.tyreManufacturers.forEach { tyreManufacturer ->
            tyreManufacturer.totalRaceEntries = 0
            tyreManufacturer.totalRaceStarts = 0
            tyreManufacturer.totalRaceWins = 0
            tyreManufacturer.totalRaceLaps = 0
            tyreManufacturer.totalPodiums = 0
            tyreManufacturer.totalPodiumRaces = 0
            tyreManufacturer.totalPolePositions = 0
            tyreManufacturer.totalFastestLaps = 0
        }

        db.circuits.forEach { circuit ->
            circuit.totalRacesHeld = 0
        }

        db.grandsPrix.forEach { grandPrix ->
            grandPrix.totalRacesHeld = 0
        }

        db.seasons.forEach { season ->

            season.constructors = season.entrants
                    ?.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.map { seasonEntrantConstructor ->
                            seasonEntrantConstructor.constructorId
                        }
                    }
                    ?.distinct()
                    ?.map { constructorId ->
                        val seasonConstructorStanding = season.constructorStandings?.firstOrNull { it.constructorId == constructorId };
                        SeasonConstructor().apply {
                            year = season.year
                            this.constructorId = constructorId
                            this.positionNumber = seasonConstructorStanding?.positionNumber
                            this.positionText = seasonConstructorStanding?.positionText
                            this.totalRaceEntries = 0
                            this.totalRaceStarts = 0
                            this.totalRaceWins = 0
                            this.total1And2Finishes = 0
                            this.totalRaceLaps = 0
                            this.totalPodiums = 0
                            this.totalPodiumRaces = 0
                            this.totalPoints = 0.toBigDecimal()
                            this.totalPolePositions = 0
                            this.totalFastestLaps = 0
                        }
                    }

            season.engineManufacturers = season.entrants
                    ?.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.map { seasonEntrantConstructor ->
                            seasonEntrantConstructor.engineManufacturerId
                        }
                    }
                    ?.distinct()
                    ?.map { engineManufacturerId ->
                        val seasonConstructorStanding = season.constructorStandings?.firstOrNull { it.engineManufacturerId == engineManufacturerId };
                        SeasonEngineManufacturer().apply {
                            year = season.year
                            this.engineManufacturerId = engineManufacturerId
                            this.positionNumber = seasonConstructorStanding?.positionNumber
                            this.positionText = seasonConstructorStanding?.positionText
                            this.totalRaceEntries = 0
                            this.totalRaceStarts = 0
                            this.totalRaceWins = 0
                            this.totalRaceLaps = 0
                            this.totalPodiums = 0
                            this.totalPodiumRaces = 0
                            this.totalPoints = 0.toBigDecimal()
                            this.totalPolePositions = 0
                            this.totalFastestLaps = 0
                        }
                    }

            season.tyreManufacturers = season.entrants
                    ?.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.flatMap { seasonEntrantConstructor ->
                            seasonEntrantConstructor.tyreManufacturers.map { it.tyreManufacturerId }
                        }
                    }
                    ?.distinct()
                    ?.map { tyreManufacturerId ->
                        SeasonTyreManufacturer().apply {
                            year = season.year
                            this.tyreManufacturerId = tyreManufacturerId
                            this.totalRaceEntries = 0
                            this.totalRaceStarts = 0
                            this.totalRaceWins = 0
                            this.totalRaceLaps = 0
                            this.totalPodiums = 0
                            this.totalPodiumRaces = 0
                            this.totalPolePositions = 0
                            this.totalFastestLaps = 0
                        }
                    }

            season.drivers = season.entrants
                    ?.flatMap { seasonEntrant ->
                        seasonEntrant.constructors.flatMap { seasonEntrantConstructor ->
                            seasonEntrantConstructor.drivers.map { it.driverId }
                        }
                    }
                    ?.distinct()
                    ?.map { driverId ->
                        val seasonDriverStanding = season.driverStandings?.firstOrNull { it.driverId == driverId };
                        SeasonDriver().apply {
                            year = season.year
                            this.driverId = driverId
                            this.positionNumber = seasonDriverStanding?.positionNumber
                            this.positionText = seasonDriverStanding?.positionText
                            this.totalRaceEntries = 0
                            this.totalRaceStarts = 0
                            this.totalRaceWins = 0
                            this.totalRaceLaps = 0
                            this.totalPodiums = 0
                            this.totalPoints = 0.toBigDecimal()
                            this.totalPolePositions = 0
                            this.totalFastestLaps = 0
                            this.totalDriverOfTheDay = 0
                            this.totalGrandSlams = 0
                        }
                    }

            season.driverStandings?.forEachIndexed { index, driverStanding ->

                driverStanding.positionDisplayOrder = index + 1

                val positionNumber = driverStanding.positionNumber
                val driver = db.drivers.first { it.id == driverStanding.driverId }

                // Best championship position.

                if (positionNumber != null && (season.year < currentSeason.year.get() || currentSeason.finished.get())) {
                    driver.bestChampionshipPosition = resolveBestPosition(driver.bestChampionshipPosition, positionNumber)
                }

                // Total championship wins.

                if (positionNumber == 1 && (season.year < currentSeason.year.get() || currentSeason.driversChampionshipDecided.get())) {
                    driver.totalChampionshipWins++
                }

                // Total championship points.

                driver.totalChampionshipPoints += driverStanding.points
            }

            season.constructorStandings?.forEachIndexed { index, constructorStanding ->

                constructorStanding.positionDisplayOrder = index + 1

                val positionNumber = constructorStanding.positionNumber
                val constructor = db.constructors.first { it.id == constructorStanding.constructorId }
                val engineManufacturer = db.engineManufacturers.first { it.id == constructorStanding.engineManufacturerId }

                // Best championship position.

                if (positionNumber != null && (season.year < currentSeason.year.get() || currentSeason.finished.get())) {
                    constructor.bestChampionshipPosition = resolveBestPosition(constructor.bestChampionshipPosition, positionNumber)
                    engineManufacturer.bestChampionshipPosition = resolveBestPosition(engineManufacturer.bestChampionshipPosition, positionNumber)
                }

                // Total championship wins.

                if (positionNumber == 1 && (season.year < currentSeason.year.get() || currentSeason.constructorsChampionshipDecided.get())) {
                    constructor.totalChampionshipWins++
                    engineManufacturer.totalChampionshipWins++
                }

                // Total championship points.

                constructor.totalChampionshipPoints += constructorStanding.points
                engineManufacturer.totalChampionshipPoints += constructorStanding.points
            }
        }

        val racesByYear = db.races.groupBy { it.year }

        racesByYear.forEach { (year, races) ->

            val season = db.seasons.first { it.year == year }

            races.forEach { race ->

                // Enrich pre-qualifying results.

                race.preQualifyingResults?.forEachIndexed { index, preQualifyingResult ->
                    preQualifyingResult.positionDisplayOrder = index + 1
                }

                // Enrich free practice 1 results.

                race.freePractice1Results?.forEachIndexed { index, freePractice1Result ->
                    freePractice1Result.positionDisplayOrder = index + 1
                }

                // Enrich free practice 2 results.

                race.freePractice2Results?.forEachIndexed { index, freePractice2Result ->
                    freePractice2Result.positionDisplayOrder = index + 1
                }

                // Enrich free practice 3 results.

                race.freePractice3Results?.forEachIndexed { index, freePractice3Result ->
                    freePractice3Result.positionDisplayOrder = index + 1
                }

                // Enrich free practice 4 results.

                race.freePractice4Results?.forEachIndexed { index, freePractice4Result ->
                    freePractice4Result.positionDisplayOrder = index + 1
                }

                // Enrich qualifying 1 results.

                race.qualifying1Results?.forEachIndexed { index, qualifying1Result ->
                    qualifying1Result.positionDisplayOrder = index + 1
                }

                // Enrich qualifying 2 results.

                race.qualifying2Results?.forEachIndexed { index, qualifying2Result ->
                    qualifying2Result.positionDisplayOrder = index + 1
                }

                // Enrich qualifying results.

                race.qualifyingResults?.forEachIndexed { index, qualifyingResult ->
                    qualifyingResult.positionDisplayOrder = index + 1
                }

                // Enrich sprint qualifying results.

                race.sprintQualifyingResults?.forEachIndexed { index, sprintQualifyingResult ->
                    sprintQualifyingResult.positionDisplayOrder = index + 1
                }

                // Enrich sprint starting grid positions.

                race.sprintStartingGridPositions?.forEachIndexed { index, sprintStartingGridPosition ->

                    sprintStartingGridPosition.positionDisplayOrder = index + 1

                    val (qualificationPositionNumber, qualificationPositionText) =
                            resolveQualificationPosition(race, sprintStartingGridPosition, true)

                    sprintStartingGridPosition.qualificationPositionNumber = qualificationPositionNumber
                    sprintStartingGridPosition.qualificationPositionText = qualificationPositionText
                }

                // Enrich sprint race results.

                race.sprintRaceResults?.forEachIndexed { index, sprintRaceResult ->

                    sprintRaceResult.positionDisplayOrder = index + 1

                    val (qualificationPositionNumber, qualificationPositionText) =
                            resolveQualificationPosition(race, sprintRaceResult, true)

                    sprintRaceResult.qualificationPositionNumber = qualificationPositionNumber
                    sprintRaceResult.qualificationPositionText = qualificationPositionText
                    sprintRaceResult.positionsGained = calculatePositionsGained(sprintRaceResult, race.sprintStartingGridPositions)
                }

                // Enrich warming-up results.

                race.warmingUpResults?.forEachIndexed { index, warmingUpResult ->
                    warmingUpResult.positionDisplayOrder = index + 1
                }

                // Enrich starting grid positions.

                race.startingGridPositions?.forEachIndexed { index, startingGridPosition ->

                    startingGridPosition.positionDisplayOrder = index + 1

                    val (qualificationPositionNumber, qualificationPositionText) =
                            resolveQualificationPosition(race, startingGridPosition, false)

                    startingGridPosition.qualificationPositionNumber = qualificationPositionNumber
                    startingGridPosition.qualificationPositionText = qualificationPositionText
                }

                // Enrich race results.

                race.raceResults?.forEachIndexed { index, raceResult ->

                    raceResult.positionDisplayOrder = index + 1

                    val (qualificationPositionNumber, qualificationPositionText) =
                            resolveQualificationPosition(race, raceResult, false)

                    raceResult.qualificationPositionNumber = qualificationPositionNumber
                    raceResult.qualificationPositionText = qualificationPositionText
                    raceResult.positionsGained = calculatePositionsGained(raceResult, race.startingGridPositions)
                    raceResult.pitStops = race.pitStops?.count { it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                    raceResult.fastestLap = race.fastestLaps?.any { it.positionNumber == 1 && it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                    raceResult.driverOfTheDay = race.driverOfTheDayResults?.any { it.positionNumber == 1 && it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                }

                // Enrich fastest laps.

                race.fastestLaps?.forEachIndexed { index, fastestLap ->
                    fastestLap.positionDisplayOrder = index + 1
                }

                // Enrich pit stops.

                race.pitStops?.forEachIndexed { index, pitStop ->
                    pitStop.positionDisplayOrder = index + 1
                }

                // Enrich Driver of the Day results.

                race.driverOfTheDayResults?.forEachIndexed { index, driverOfTheDayResult ->
                    driverOfTheDayResult.positionDisplayOrder = index + 1
                }

                // Enrich driver standings.

                race.driverStandings?.forEachIndexed { index, driverStanding ->
                    driverStanding.positionDisplayOrder = index + 1
                }

                // Enrich constructor standings.

                race.constructorStandings?.forEachIndexed { index, constructorStanding ->
                    constructorStanding.positionDisplayOrder = index + 1
                }

                // Total points.

                ((race.raceResults ?: emptyList()) + (race.sprintRaceResults ?: emptyList())).forEach { raceResult ->
                    if (raceResult.points != null) {

                        val driver = db.drivers.first { it.id == raceResult.driverId }
                        val constructor = db.constructors.first { it.id == raceResult.constructorId }
                        val engineManufacturer = db.engineManufacturers.first { it.id == raceResult.engineManufacturerId }

                        val seasonDriver = season.drivers.first { it.driverId == raceResult.driverId }
                        val seasonConstructor = season.constructors.first { it.constructorId == raceResult.constructorId }
                        val seasonEngineManufacturer = season.engineManufacturers.first { it.engineManufacturerId == raceResult.engineManufacturerId }

                        driver.totalPoints += raceResult.points

                        if (year > 1957) {
                            constructor.totalPoints += raceResult.points
                            engineManufacturer.totalPoints += raceResult.points
                        }

                        seasonDriver.totalPoints += raceResult.points
                        seasonConstructor.totalPoints += raceResult.points
                        seasonEngineManufacturer.totalPoints += raceResult.points
                    }
                }

                // Total races held.

                if (race.date.isBefore(LocalDate.now().plusDays(1))) {

                    val grandPrix = db.grandsPrix.first { it.id == race.grandPrixId }
                    val circuit = db.circuits.first { it.id == race.circuitId }

                    grandPrix.totalRacesHeld++
                    circuit.totalRacesHeld++
                }

                val raceEntries = mutableListOf<Any>()
                val raceStarts = mutableListOf<Any>()
                val raceWins = mutableListOf<Any>()
                val podiums = mutableListOf<Any>()

                race.sprintStartingGridPositions?.forEach { sprintStartingGridPosition ->

                    if (sprintStartingGridPosition.positionNumber == 1 && race.year == 2022) {

                        val driver = db.drivers.first { it.id == sprintStartingGridPosition.driverId }
                        val constructor = db.constructors.first { it.id == sprintStartingGridPosition.constructorId }
                        val engineManufacturer = db.engineManufacturers.first { it.id == sprintStartingGridPosition.engineManufacturerId }
                        val tyreManufacturer = db.tyreManufacturers.first { it.id == sprintStartingGridPosition.tyreManufacturerId }

                        val seasonDriver = season.drivers.first { it.driverId == sprintStartingGridPosition.driverId }
                        val seasonConstructor = season.constructors.first { it.constructorId == sprintStartingGridPosition.constructorId }
                        val seasonEngineManufacturer = season.engineManufacturers.first { it.engineManufacturerId == sprintStartingGridPosition.engineManufacturerId }
                        val seasonTyreManufacturer = season.tyreManufacturers.first { it.tyreManufacturerId == sprintStartingGridPosition.tyreManufacturerId }

                        // Total pole positions.

                        driver.totalPolePositions++
                        constructor.totalPolePositions++
                        engineManufacturer.totalPolePositions++
                        tyreManufacturer.totalPolePositions++

                        seasonDriver.totalPolePositions++
                        seasonConstructor.totalPolePositions++
                        seasonEngineManufacturer.totalPolePositions++
                        seasonTyreManufacturer.totalPolePositions++
                    }
                }

                race.startingGridPositions?.forEach { startingGridPosition ->

                    val driver = db.drivers.first { it.id == startingGridPosition.driverId }
                    val constructor = db.constructors.first { it.id == startingGridPosition.constructorId }
                    val engineManufacturer = db.engineManufacturers.first { it.id == startingGridPosition.engineManufacturerId }
                    val tyreManufacturer = db.tyreManufacturers.first { it.id == startingGridPosition.tyreManufacturerId }

                    val seasonDriver = season.drivers.first { it.driverId == startingGridPosition.driverId }
                    val seasonConstructor = season.constructors.first { it.constructorId == startingGridPosition.constructorId }
                    val seasonEngineManufacturer = season.engineManufacturers.first { it.engineManufacturerId == startingGridPosition.engineManufacturerId }
                    val seasonTyreManufacturer = season.tyreManufacturers.first { it.tyreManufacturerId == startingGridPosition.tyreManufacturerId }

                    // Best starting grid position.

                    if (startingGridPosition.positionNumber != null) {
                        val positionNumber = startingGridPosition.positionNumber

                        driver.bestStartingGridPosition = resolveBestPosition(driver.bestStartingGridPosition, positionNumber)
                        constructor.bestStartingGridPosition = resolveBestPosition(constructor.bestStartingGridPosition, positionNumber)
                        engineManufacturer.bestStartingGridPosition = resolveBestPosition(engineManufacturer.bestStartingGridPosition, positionNumber)
                        tyreManufacturer.bestStartingGridPosition = resolveBestPosition(tyreManufacturer.bestStartingGridPosition, positionNumber)

                        seasonDriver.bestStartingGridPosition = resolveBestPosition(seasonDriver.bestStartingGridPosition, positionNumber)
                        seasonConstructor.bestStartingGridPosition = resolveBestPosition(seasonConstructor.bestStartingGridPosition, positionNumber)
                        seasonEngineManufacturer.bestStartingGridPosition = resolveBestPosition(seasonEngineManufacturer.bestStartingGridPosition, positionNumber)
                        seasonTyreManufacturer.bestStartingGridPosition = resolveBestPosition(seasonTyreManufacturer.bestStartingGridPosition, positionNumber)
                    }

                    // Total pole positions.

                    if (startingGridPosition.positionNumber == 1 && (race.year < 2022 || race.year > 2022 || race.qualifyingFormat != Race.QualifyingFormat.SPRINT_RACE)) {
                        driver.totalPolePositions++
                        constructor.totalPolePositions++
                        engineManufacturer.totalPolePositions++
                        tyreManufacturer.totalPolePositions++

                        seasonDriver.totalPolePositions++
                        seasonConstructor.totalPolePositions++
                        seasonEngineManufacturer.totalPolePositions++
                        seasonTyreManufacturer.totalPolePositions++
                    }
                }

                race.raceResults?.forEach { raceResult ->

                    val driver = db.drivers.first { it.id == raceResult.driverId }
                    val constructor = db.constructors.first { it.id == raceResult.constructorId }
                    val engineManufacturer = db.engineManufacturers.first { it.id == raceResult.engineManufacturerId }
                    val tyreManufacturer = db.tyreManufacturers.first { it.id == raceResult.tyreManufacturerId }

                    val seasonDriver = season.drivers.first { it.driverId == raceResult.driverId }
                    val seasonConstructor = season.constructors.first { it.constructorId == raceResult.constructorId }
                    val seasonEngineManufacturer = season.engineManufacturers.first { it.engineManufacturerId == raceResult.engineManufacturerId }
                    val seasonTyreManufacturer = season.tyreManufacturers.first { it.tyreManufacturerId == raceResult.tyreManufacturerId }

                    // Best race result.

                    if (raceResult.positionNumber != null) {
                        val positionNumber = raceResult.positionNumber

                        driver.bestRaceResult = resolveBestPosition(driver.bestRaceResult, positionNumber)
                        constructor.bestRaceResult = resolveBestPosition(constructor.bestRaceResult, positionNumber)
                        engineManufacturer.bestRaceResult = resolveBestPosition(engineManufacturer.bestRaceResult, positionNumber)
                        tyreManufacturer.bestRaceResult = resolveBestPosition(tyreManufacturer.bestRaceResult, positionNumber)

                        seasonDriver.bestRaceResult = resolveBestPosition(seasonDriver.bestRaceResult, positionNumber)
                        seasonConstructor.bestRaceResult = resolveBestPosition(seasonConstructor.bestRaceResult, positionNumber)
                        seasonEngineManufacturer.bestRaceResult = resolveBestPosition(seasonEngineManufacturer.bestRaceResult, positionNumber)
                        seasonTyreManufacturer.bestRaceResult = resolveBestPosition(seasonTyreManufacturer.bestRaceResult, positionNumber)
                    }

                    // Total race entries.

                    if (addIfAbsent(raceEntries, driver)) {
                        driver.totalRaceEntries++
                        seasonDriver.totalRaceEntries++
                    }

                    if (addIfAbsent(raceEntries, constructor)) {
                        constructor.totalRaceEntries++
                        seasonConstructor.totalRaceEntries++
                    }

                    if (addIfAbsent(raceEntries, engineManufacturer)) {
                        engineManufacturer.totalRaceEntries++
                        seasonEngineManufacturer.totalRaceEntries++
                    }

                    if (addIfAbsent(raceEntries, tyreManufacturer)) {
                        tyreManufacturer.totalRaceEntries++
                        seasonTyreManufacturer.totalRaceEntries++
                    }

                    // Total race starts.

                    if (raceResult.positionText !in listOf("DNP", "DNPQ", "DNQ", "DNS", "EX")) {

                        if (addIfAbsent(raceStarts, driver)) {
                            driver.totalRaceStarts++
                            seasonDriver.totalRaceStarts++
                        }

                        if (addIfAbsent(raceStarts, constructor)) {
                            constructor.totalRaceStarts++
                            seasonConstructor.totalRaceStarts++
                        }

                        if (addIfAbsent(raceStarts, engineManufacturer)) {
                            engineManufacturer.totalRaceStarts++
                            seasonEngineManufacturer.totalRaceStarts++
                        }

                        if (addIfAbsent(raceStarts, tyreManufacturer)) {
                            tyreManufacturer.totalRaceStarts++
                            seasonTyreManufacturer.totalRaceStarts++
                        }
                    }

                    // Total race wins.

                    if (raceResult.positionNumber == 1) {

                        if (addIfAbsent(raceWins, driver)) {
                            driver.totalRaceWins++
                            seasonDriver.totalRaceWins++
                        }

                        if (addIfAbsent(raceWins, constructor)) {
                            constructor.totalRaceWins++
                            seasonConstructor.totalRaceWins++
                        }

                        if (addIfAbsent(raceWins, engineManufacturer)) {
                            engineManufacturer.totalRaceWins++
                            seasonEngineManufacturer.totalRaceWins++
                        }

                        if (addIfAbsent(raceWins, tyreManufacturer)) {
                            tyreManufacturer.totalRaceWins++
                            seasonTyreManufacturer.totalRaceWins++
                        }

                        // Total 1 and 2 finishes.

                        if (!raceResult.sharedCar) {
                            val position2 = race.raceResults.firstOrNull { it.positionNumber == 2 }
                            if (position2 != null && position2.constructorId == constructor.id) {
                                constructor.total1And2Finishes++
                                seasonConstructor.total1And2Finishes++
                            }
                        }
                    }

                    // Total race laps.

                    if (raceResult.laps != null) {

                        driver.totalRaceLaps += raceResult.laps
                        seasonDriver.totalRaceLaps += raceResult.laps

                        if (!raceResult.sharedCar) {

                            constructor.totalRaceLaps += raceResult.laps
                            engineManufacturer.totalRaceLaps += raceResult.laps
                            tyreManufacturer.totalRaceLaps += raceResult.laps

                            seasonConstructor.totalRaceLaps += raceResult.laps
                            seasonEngineManufacturer.totalRaceLaps += raceResult.laps
                            seasonTyreManufacturer.totalRaceLaps += raceResult.laps
                        }
                    }

                    // Total podiums + podium races.

                    if (raceResult.positionNumber in listOf(1, 2, 3)) {

                        if (addIfAbsent(podiums, driver)) {
                            driver.totalPodiums++
                            seasonDriver.totalPodiums++
                        }

                        if (!raceResult.sharedCar) {

                            constructor.totalPodiums++
                            engineManufacturer.totalPodiums++
                            tyreManufacturer.totalPodiums++

                            seasonConstructor.totalPodiums++
                            seasonEngineManufacturer.totalPodiums++
                            seasonTyreManufacturer.totalPodiums++

                            if (addIfAbsent(podiums, constructor)) {
                                constructor.totalPodiumRaces++
                                seasonConstructor.totalPodiumRaces++
                            }

                            if (addIfAbsent(podiums, engineManufacturer)) {
                                engineManufacturer.totalPodiumRaces++
                                seasonEngineManufacturer.totalPodiumRaces++
                            }

                            if (addIfAbsent(podiums, tyreManufacturer)) {
                                tyreManufacturer.totalPodiumRaces++
                                seasonTyreManufacturer.totalPodiumRaces++
                            }
                        }
                    }

                    // Total fastest laps.

                    if (raceResult.fastestLap == true) {

                        driver.totalFastestLaps++
                        constructor.totalFastestLaps++
                        engineManufacturer.totalFastestLaps++
                        tyreManufacturer.totalFastestLaps++

                        seasonDriver.totalFastestLaps++
                        seasonConstructor.totalFastestLaps++
                        seasonEngineManufacturer.totalFastestLaps++
                        seasonTyreManufacturer.totalFastestLaps++
                    }

                    // Total Driver of the Day.

                    if (raceResult.driverOfTheDay == true) {
                        driver.totalDriverOfTheDay++
                        seasonDriver.totalDriverOfTheDay++
                    }

                    // Total Grand Slams.

                    if (raceResult.grandSlam == true) {
                        driver.totalGrandSlams++
                        seasonDriver.totalGrandSlams++
                    }
                }

                if (race.round > 1) {

                    val previousRace = db.races.first { it.year == race.year && it.round == race.round - 1 }

                    // Driver standing positions gained.

                    race?.driverStandings?.forEach { driverStanding ->
                        if (driverStanding.positionNumber != null) {
                            val previousRaceDriverStanding = previousRace.driverStandings.firstOrNull { it.driverId == driverStanding.driverId }
                            if (previousRaceDriverStanding?.positionNumber != null) {
                                driverStanding.positionsGained = previousRaceDriverStanding.positionNumber - driverStanding.positionNumber
                            } else {
                                driverStanding.positionsGained = (previousRace.driverStandings.size + 1) - driverStanding.positionNumber
                            }
                        }
                    }

                    // Constructor standing positions gained.

                    race?.constructorStandings?.forEach { constructorStanding ->
                        if (constructorStanding.positionNumber != null) {
                            val previousRaceConstructorStanding = previousRace.constructorStandings.firstOrNull { it.constructorId == constructorStanding.constructorId && it.engineManufacturerId == constructorStanding.engineManufacturerId }
                            if (previousRaceConstructorStanding?.positionNumber != null) {
                                constructorStanding.positionsGained = previousRaceConstructorStanding.positionNumber - constructorStanding.positionNumber
                            } else {
                                constructorStanding.positionsGained = (previousRace.constructorStandings.size + 1) - constructorStanding.positionNumber
                            }
                        }
                    }
                }
            }
        }

        return db
    }

    private fun resolveBestPosition(currentBestPosition: Int?, newPosition: Int): Int {
        return min(currentBestPosition ?: newPosition, newPosition)
    }

    private fun resolveQualificationPosition(race: Race, startingGridPosition: StartingGridPosition, sprint: Boolean): Pair<Int?, String?> {
        return resolveQualificationPosition(
                race,
                startingGridPosition.driverNumber,
                startingGridPosition.driverId,
                startingGridPosition.constructorId,
                startingGridPosition.engineManufacturerId,
                startingGridPosition.tyreManufacturerId,
                sprint
        )
    }

    private fun resolveQualificationPosition(race: Race, raceResult: RaceResult, sprint: Boolean): Pair<Int?, String?> {
        if (raceResult.sharedCar) {
            return Pair(null, null)
        }
        return resolveQualificationPosition(
                race,
                raceResult.driverNumber,
                raceResult.driverId,
                raceResult.constructorId,
                raceResult.engineManufacturerId,
                raceResult.tyreManufacturerId,
                sprint
        )
    }

    private fun resolveQualificationPosition(
            race: Race,
            driverNumber: String,
            driverId: String,
            constructorId: String,
            engineManufacturerId: String,
            tyreManufacturerId: String,
            sprint: Boolean
    ): Pair<Int?, String?> {
        val qualificationPositionNumber: Int?
        val qualificationPositionText: String?
        if (race.qualifyingFormat == Race.QualifyingFormat.SPRINT_RACE) {
            if (sprint) {
                val qualifyingResult = race.qualifyingResults.firstOrNull { it.driverNumber == driverNumber && it.driverId == driverId && it.constructorId == constructorId && it.engineManufacturerId == engineManufacturerId && it.tyreManufacturerId == tyreManufacturerId }
                qualificationPositionNumber = qualifyingResult?.positionNumber
                qualificationPositionText = qualifyingResult?.positionText
            } else {
                val sprintRaceResult = race.sprintRaceResults.firstOrNull { it.driverNumber == driverNumber && it.driverId == driverId && it.constructorId == constructorId && it.engineManufacturerId == engineManufacturerId && it.tyreManufacturerId == tyreManufacturerId }
                qualificationPositionNumber = sprintRaceResult?.positionNumber
                qualificationPositionText = sprintRaceResult?.positionText
            }
        } else if (race.sprintQualifyingFormat == Race.SprintQualifyingFormat.SPRINT_SHOOTOUT) {
            if (sprint) {
                val sprintQualifyingResult = race.sprintQualifyingResults.firstOrNull { it.driverNumber == driverNumber && it.driverId == driverId && it.constructorId == constructorId && it.engineManufacturerId == engineManufacturerId && it.tyreManufacturerId == tyreManufacturerId }
                qualificationPositionNumber = sprintQualifyingResult?.positionNumber
                qualificationPositionText = sprintQualifyingResult?.positionText
            } else {
                val qualifyingResults = race.qualifyingResults.firstOrNull { it.driverNumber == driverNumber && it.driverId == driverId && it.constructorId == constructorId && it.engineManufacturerId == engineManufacturerId && it.tyreManufacturerId == tyreManufacturerId }
                qualificationPositionNumber = qualifyingResults?.positionNumber
                qualificationPositionText = qualifyingResults?.positionText
            }
        } else {
            val qualifyingResult = race.qualifyingResults.firstOrNull { it.driverNumber == driverNumber && it.driverId == driverId && it.constructorId == constructorId && it.engineManufacturerId == engineManufacturerId && it.tyreManufacturerId == tyreManufacturerId }
            qualificationPositionNumber = qualifyingResult?.positionNumber
            qualificationPositionText = qualifyingResult?.positionText
        }
        return Pair(qualificationPositionNumber, qualificationPositionText)
    }

    private fun calculatePositionsGained(raceResult: RaceResult, startingGridPositions: List<StartingGridPosition>): Int? {
        var positionsGained: Int? = null
        if (raceResult.positionNumber != null && !raceResult.sharedCar) {
            if (raceResult.gridPositionNumber != null) {
                positionsGained = raceResult.gridPositionNumber - raceResult.positionNumber
            } else {
                val index =
                        startingGridPositions.indexOfFirst { it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                if (index > -1) {
                    positionsGained = (index + 1) - raceResult.positionNumber
                }
            }
        }
        return positionsGained
    }

    private fun <T> addIfAbsent(collection: MutableCollection<T>, element: T): Boolean {
        return if (collection.contains(element)) {
            false
        } else {
            collection.add(element)
            true
        }
    }

    private fun createMapper(): ObjectMapper {
        val simpleModule = SimpleModule()
        simpleModule.addDeserializer(SeasonEntrant::class.java, SeasonEntrantDeserializer())
        simpleModule.addDeserializer(SeasonEntrantConstructor::class.java, SeasonEntrantConstructorDeserializer())
        simpleModule.setMixInAnnotation(SeasonEntrantDriver::class.java, SeasonEntrantDriverMixIn::class.java)
        simpleModule.setMixInAnnotation(SeasonDriverStanding::class.java, SeasonDriverStandingMixIn::class.java)
        simpleModule.setMixInAnnotation(SeasonConstructorStanding::class.java, SeasonConstructorStandingMixIn::class.java)
        simpleModule.setMixInAnnotation(PracticeResult::class.java, PracticeResultMixIn::class.java)
        simpleModule.setMixInAnnotation(QualifyingResult::class.java, QualifyingResultMixIn::class.java)
        simpleModule.setMixInAnnotation(StartingGridPosition::class.java, StartingGridPositionMixIn::class.java)
        simpleModule.setMixInAnnotation(RaceResult::class.java, RaceResultMixIn::class.java)
        simpleModule.setMixInAnnotation(FastestLap::class.java, FastestLapMixIn::class.java)
        simpleModule.setMixInAnnotation(PitStop::class.java, PitStopMixIn::class.java)
        simpleModule.setMixInAnnotation(DriverOfTheDayResult::class.java, DriverOfTheDayResultMixIn::class.java)
        simpleModule.setMixInAnnotation(RaceDriverStanding::class.java, RaceDriverStandingMixIn::class.java)
        simpleModule.setMixInAnnotation(RaceConstructorStanding::class.java, RaceConstructorStandingMixIn::class.java)
        val mapper = YAMLMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
                .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
                .addModule(JavaTimeModule())
                .addModule(KotlinModule.Builder().build())
                .addModule(simpleModule)
                .build()
        return mapper
    }

    private inline fun <reified T : Any> readValue(parent: File, path: String): T {
        return mapper.readValue(File(parent, path))
    }

    private inline fun <reified T : Any> readValues(path: String): List<T>? {
        return readValues<T>(sourceDir, path)
    }

    private inline fun <reified T : Any> readValues(parent: File, path: String): List<T>? {
        try {
            val data = mutableListOf<T>()
            val file = File(parent, path)
            if (file.isDirectory) {
                file.listFiles().sortedArray().forEach {
                    val value = mapper.readValue<T>(it)
                    data.add(value)
                }
            } else {
                val collectionType = mapper.typeFactory.constructCollectionType(List::class.java, T::class.java)
                val values = mapper.readValue<List<T>>(file, collectionType)
                data.addAll(values)
            }
            return data
        } catch (e: FileNotFoundException) {
            return null
        } catch (e: MismatchedInputException) {
            if (e.message != null && e.message!!.startsWith("No content to map due to end-of-input")) {
                return emptyList()
            } else {
                throw e
            }
        }
    }
}
