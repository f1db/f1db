package com.onlyf1.db.plugin.reader

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.onlyf1.db.plugin.CurrentSeason
import com.onlyf1.db.plugin.reader.databind.DriverOfTheDayResultMixIn
import com.onlyf1.db.plugin.reader.databind.FastestLapMixIn
import com.onlyf1.db.plugin.reader.databind.PitStopMixIn
import com.onlyf1.db.plugin.reader.databind.PracticeResultMixIn
import com.onlyf1.db.plugin.reader.databind.QualifyingResultMixIn
import com.onlyf1.db.plugin.reader.databind.RaceConstructorStandingMixIn
import com.onlyf1.db.plugin.reader.databind.RaceDriverStandingMixIn
import com.onlyf1.db.plugin.reader.databind.RaceResultMixIn
import com.onlyf1.db.plugin.reader.databind.SeasonConstructorStandingMixIn
import com.onlyf1.db.plugin.reader.databind.SeasonDriverStandingMixIn
import com.onlyf1.db.plugin.reader.databind.SeasonEntrantConstructorDeserializer
import com.onlyf1.db.plugin.reader.databind.SeasonEntrantDeserializer
import com.onlyf1.db.plugin.reader.databind.SeasonEntrantDriverMixIn
import com.onlyf1.db.plugin.reader.databind.StartingGridPositionMixIn
import com.onlyf1.db.schema.single.DriverOfTheDayResult
import com.onlyf1.db.schema.single.FastestLap
import com.onlyf1.db.schema.single.OnlyF1DB
import com.onlyf1.db.schema.single.PitStop
import com.onlyf1.db.schema.single.PracticeResult
import com.onlyf1.db.schema.single.QualifyingResult
import com.onlyf1.db.schema.single.Race
import com.onlyf1.db.schema.single.RaceConstructorStanding
import com.onlyf1.db.schema.single.RaceDriverStanding
import com.onlyf1.db.schema.single.RaceResult
import com.onlyf1.db.schema.single.Season
import com.onlyf1.db.schema.single.SeasonConstructorStanding
import com.onlyf1.db.schema.single.SeasonDriverStanding
import com.onlyf1.db.schema.single.SeasonEntrant
import com.onlyf1.db.schema.single.SeasonEntrantConstructor
import com.onlyf1.db.schema.single.SeasonEntrantDriver
import com.onlyf1.db.schema.single.StartingGridPosition
import java.io.File
import java.io.FileNotFoundException
import java.time.LocalDate

/**
 * The OnlyF1-DB (raw data) reader.
 *
 * @author Marcel Overdijk
 */
class OnlyF1DBReader(
    private val sourceDir: File,
    private val currentSeason: CurrentSeason,
) {

    private val mapper = createMapper()

    fun read(): OnlyF1DB {

        println("Reading raw data....")

        val db = OnlyF1DB()

        // Read simple types.

        db.continents = readValues("continents")
        db.countries = readValues("countries")
        db.drivers = readValues("drivers")
        db.constructors = readValues("constructors")
        db.engineManufacturers = readValues("engine-manufacturers")
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

            // Read races

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
        }

        db.constructors.forEach { constructor ->
            constructor.totalChampionshipWins = 0
            constructor.totalRaceEntries = 0
            constructor.totalRaceStarts = 0
            constructor.totalRaceWins = 0
            constructor.total1And2Finishes = 0
            constructor.totalRaceLaps = 0
            constructor.totalPodiums = 0
            constructor.totalPodiumRaces = 0
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

            season.driverStandings?.forEach { driverStanding ->

                val positionNumber = driverStanding.positionNumber
                val driver = db.drivers.first { it.id == driverStanding.driverId }

                // Best championship position.

                if (positionNumber != null && (season.year < currentSeason.year.get() || currentSeason.finished.get())) {
                    driver.bestChampionshipPosition = Math.min(driver.bestChampionshipPosition ?: positionNumber, positionNumber)
                }

                // Total championship wins.

                if (positionNumber == 1 && (season.year < currentSeason.year.get() || currentSeason.driversChampionshipDecided.get())) {
                    driver.totalChampionshipWins++
                }

                // Total championship points.

                driver.totalChampionshipPoints += driverStanding.points
            }

            season.constructorStandings?.forEach { constructorStanding ->

                val positionNumber = constructorStanding.positionNumber
                val constructor = db.constructors.first { it.id == constructorStanding.constructorId }
                val engineManufacturer = db.engineManufacturers.first { it.id == constructorStanding.engineManufacturerId }

                // Best championship position.

                if (positionNumber != null && (season.year < currentSeason.year.get() || currentSeason.finished.get())) {
                    constructor.bestChampionshipPosition = Math.min(constructor.bestChampionshipPosition ?: positionNumber, positionNumber)
                    engineManufacturer.bestChampionshipPosition = Math.min(engineManufacturer.bestChampionshipPosition ?: positionNumber, positionNumber)
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

        db.races.forEach { race ->

            // Enrich sprint qualifying results.

            race.sprintRaceResults?.forEach { sprintRaceResult ->
                sprintRaceResult.positionsGained = calculatePositionsGained(sprintRaceResult, race.sprintStartingGridPositions)
            }

            // Enrich race results.

            race.raceResults?.forEach { raceResult ->
                raceResult.positionsGained = calculatePositionsGained(raceResult, race.startingGridPositions)
                raceResult.fastestLap =
                    race.fastestLaps?.any { it.positionNumber == 1 && it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                raceResult.pitStops =
                    race.pitStops?.count { it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                raceResult.driverOfTheDay =
                    race.driverOfTheDayResults?.any { it.positionNumber == 1 && it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
            }

            // Total points.

            ((race.raceResults ?: emptyList()) + (race.sprintRaceResults ?: emptyList())).forEach { raceResult ->
                if (raceResult.points != null) {
                    val driver = db.drivers.first { it.id == raceResult.driverId }
                    driver.totalPoints += raceResult.points
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

                    // Total pole positions.

                    driver.totalPolePositions++
                    constructor.totalPolePositions++
                    engineManufacturer.totalPolePositions++
                    tyreManufacturer.totalPolePositions++
                }
            }

            race.startingGridPositions?.forEach { startingGridPosition ->

                val driver = db.drivers.first { it.id == startingGridPosition.driverId }
                val constructor = db.constructors.first { it.id == startingGridPosition.constructorId }
                val engineManufacturer = db.engineManufacturers.first { it.id == startingGridPosition.engineManufacturerId }
                val tyreManufacturer = db.tyreManufacturers.first { it.id == startingGridPosition.tyreManufacturerId }

                // Best starting grid position.

                if (startingGridPosition.positionNumber != null) {
                    driver.bestStartingGridPosition =
                        Math.min(driver.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                    constructor.bestStartingGridPosition =
                        Math.min(constructor.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                    engineManufacturer.bestStartingGridPosition =
                        Math.min(engineManufacturer.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                    tyreManufacturer.bestStartingGridPosition =
                        Math.min(tyreManufacturer.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                }

                // Total pole positions.

                if (startingGridPosition.positionNumber == 1 && (race.year < 2022 || race.year > 2022 || race.qualifyingFormat != Race.QualifyingFormat.SPRINT_RACE)) {
                    driver.totalPolePositions++
                    constructor.totalPolePositions++
                    engineManufacturer.totalPolePositions++
                    tyreManufacturer.totalPolePositions++
                }
            }

            race.raceResults?.forEach { raceResult ->

                val driver = db.drivers.first { it.id == raceResult.driverId }
                val constructor = db.constructors.first { it.id == raceResult.constructorId }
                val engineManufacturer = db.engineManufacturers.first { it.id == raceResult.engineManufacturerId }
                val tyreManufacturer = db.tyreManufacturers.first { it.id == raceResult.tyreManufacturerId }

                // Best race result.

                if (raceResult.positionNumber != null) {
                    driver.bestRaceResult = Math.min(driver.bestRaceResult ?: raceResult.positionNumber, raceResult.positionNumber)
                    constructor.bestRaceResult = Math.min(constructor.bestRaceResult ?: raceResult.positionNumber, raceResult.positionNumber)
                    engineManufacturer.bestRaceResult = Math.min(engineManufacturer.bestRaceResult ?: raceResult.positionNumber, raceResult.positionNumber)
                    tyreManufacturer.bestRaceResult = Math.min(tyreManufacturer.bestRaceResult ?: raceResult.positionNumber, raceResult.positionNumber)
                }

                // Total race entries.

                driver.totalRaceEntries += incrementIfAbsent(raceEntries, driver)
                constructor.totalRaceEntries += incrementIfAbsent(raceEntries, constructor)
                engineManufacturer.totalRaceEntries += incrementIfAbsent(raceEntries, engineManufacturer)
                tyreManufacturer.totalRaceEntries += incrementIfAbsent(raceEntries, tyreManufacturer)

                // Total race starts.

                if (raceResult.positionText !in listOf("DNP", "DNPQ", "DNQ", "DNS", "EX")) {
                    driver.totalRaceStarts += incrementIfAbsent(raceStarts, driver)
                    constructor.totalRaceStarts += incrementIfAbsent(raceStarts, constructor)
                    engineManufacturer.totalRaceStarts += incrementIfAbsent(raceStarts, engineManufacturer)
                    tyreManufacturer.totalRaceStarts += incrementIfAbsent(raceStarts, tyreManufacturer)
                }

                // Total race wins.

                if (raceResult.positionNumber == 1) {
                    driver.totalRaceWins += incrementIfAbsent(raceWins, driver)
                    constructor.totalRaceWins += incrementIfAbsent(raceWins, constructor)
                    engineManufacturer.totalRaceWins += incrementIfAbsent(raceWins, engineManufacturer)
                    tyreManufacturer.totalRaceWins += incrementIfAbsent(raceWins, tyreManufacturer)

                    // Total 1 and 2 finishes.

                    if (!raceResult.sharedCar) {
                        val position2 = race.raceResults.firstOrNull { it.positionNumber == 2 }
                        if (position2 != null && position2.constructorId == constructor.id) {
                            constructor.total1And2Finishes++
                        }
                    }
                }

                // Total race laps.

                if (raceResult.laps != null) {
                    driver.totalRaceLaps += raceResult.laps
                    if (!raceResult.sharedCar) {
                        constructor.totalRaceLaps += raceResult.laps
                        engineManufacturer.totalRaceLaps += raceResult.laps
                        tyreManufacturer.totalRaceLaps += raceResult.laps
                    }
                }

                // Total podiums + podium races.

                if (raceResult.positionNumber in listOf(1, 2, 3)) {
                    driver.totalPodiums += incrementIfAbsent(podiums, driver)
                    if (!raceResult.sharedCar) {
                        constructor.totalPodiums++
                        constructor.totalPodiumRaces += incrementIfAbsent(podiums, constructor)
                        engineManufacturer.totalPodiums++
                        engineManufacturer.totalPodiumRaces += incrementIfAbsent(podiums, engineManufacturer)
                        tyreManufacturer.totalPodiums++
                        tyreManufacturer.totalPodiumRaces += incrementIfAbsent(podiums, tyreManufacturer)
                    }
                }

                // Total fastest laps.

                if (raceResult.fastestLap == true) {
                    driver.totalFastestLaps++
                    constructor.totalFastestLaps++
                    engineManufacturer.totalFastestLaps++
                    tyreManufacturer.totalFastestLaps++
                }

                // Total driver of the day.

                if (raceResult.driverOfTheDay == true) {
                    driver.totalDriverOfTheDay++
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
                        val previousRaceConstructorStanding =
                            previousRace.constructorStandings.firstOrNull { it.constructorId == constructorStanding.constructorId && it.engineManufacturerId == constructorStanding.engineManufacturerId }
                        if (previousRaceConstructorStanding?.positionNumber != null) {
                            constructorStanding.positionsGained = previousRaceConstructorStanding.positionNumber - constructorStanding.positionNumber
                        } else {
                            constructorStanding.positionsGained = (previousRace.constructorStandings.size + 1) - constructorStanding.positionNumber
                        }
                    }
                }
            }
        }

        return db
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

    private fun <T> incrementIfAbsent(collection: MutableCollection<T>, element: T): Int {
        if (collection.contains(element)) {
            return 0
        } else {
            collection.add(element)
            return 1
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
