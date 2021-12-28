package net.f1db.gradle.plugin

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import net.f1db.Circuit
import net.f1db.Constructor
import net.f1db.ConstructorStanding
import net.f1db.Continent
import net.f1db.Country
import net.f1db.Driver
import net.f1db.DriverOfTheDayResult
import net.f1db.DriverStanding
import net.f1db.EngineManufacturer
import net.f1db.Entrant
import net.f1db.F1db
import net.f1db.FastestLap
import net.f1db.GrandPrix
import net.f1db.PitStop
import net.f1db.PracticeResult
import net.f1db.QualifyingResult
import net.f1db.Race
import net.f1db.RaceResult
import net.f1db.Season
import net.f1db.SeasonEntrant
import net.f1db.SeasonEntrantConstructor
import net.f1db.SeasonEntrantDriver
import net.f1db.StartingGridPosition
import net.f1db.TyreManufacturer
import net.f1db.gradle.plugin.databind.ConstructorStandingMixIn
import net.f1db.gradle.plugin.databind.DriverOfTheDayResultMixIn
import net.f1db.gradle.plugin.databind.DriverStandingMixIn
import net.f1db.gradle.plugin.databind.FastestLapMixIn
import net.f1db.gradle.plugin.databind.PitStopMixIn
import net.f1db.gradle.plugin.databind.PracticeResultMixIn
import net.f1db.gradle.plugin.databind.QualifyingResultMixIn
import net.f1db.gradle.plugin.databind.RaceResultMixIn
import net.f1db.gradle.plugin.databind.SeasonEntrantConstructorDeserializer
import net.f1db.gradle.plugin.databind.SeasonEntrantDeserializer
import net.f1db.gradle.plugin.databind.SeasonEntrantDriverMixIn
import net.f1db.gradle.plugin.databind.StartingGridPositionMixIn

import java.time.Duration

/**
 * The F1DB data reader.
 *
 * @author Marcel Overdijk
 */
class F1DBReader {

    int currentSeason
    boolean wdcDecided
    boolean wccDecided
    int wccFirstYear = 1958
    File sourceDir
    ObjectMapper mapper

    F1DBReader(File projectDir, F1DBPluginExtension extension) {
        this.currentSeason = extension.currentSeason
        this.wdcDecided = extension.wdcDecided
        this.wccDecided = extension.wccDecided
        this.sourceDir = new File(projectDir, "src/data")
        this.mapper = createMapper()
    }

    F1db read() {

        println "Reading raw data....."

        def f1db = new F1db()

        // Read simple types.

        f1db.continents = readValues(sourceDir, "continents", Continent)
        f1db.countries = readValues(sourceDir, "countries", Country)
        f1db.drivers = readValues(sourceDir, "drivers", Driver)
        f1db.constructors = readValues(sourceDir, "constructors", Constructor)
        f1db.engineManufacturers = readValues(sourceDir, "engine-manufacturers", EngineManufacturer)
        f1db.tyreManufacturers = readValues(sourceDir, "tyre-manufacturers", TyreManufacturer)
        f1db.entrants = readValues(sourceDir, "entrants", Entrant)
        f1db.circuits = readValues(sourceDir, "circuits", Circuit)
        f1db.grandsPrix = readValues(sourceDir, "grands-prix", GrandPrix)
        f1db.seasons = []

        // Read seasons.

        listFiles(sourceDir, "seasons").each { seasonDir ->

            // Read season.

            def season = new Season()
            season.year = seasonDir.name.toInteger()
            season.entrants = readValues(seasonDir, "entrants.yml", SeasonEntrant)
            season.races = []

            // Read races

            listFiles(seasonDir, "races").each { raceDir ->

                // Read race + race data + standings.

                def race = readValue(raceDir, "race.yml", Race)
                race.year = season.year
                race.preQualifyingResults = readValues(raceDir, "pre-qualifying-results.yml", QualifyingResult)
                race.freePractice1Results = readValues(raceDir, "free-practice-1-results.yml", PracticeResult)
                race.freePractice2Results = readValues(raceDir, "free-practice-2-results.yml", PracticeResult)
                race.freePractice3Results = readValues(raceDir, "free-practice-3-results.yml", PracticeResult)
                race.freePractice4Results = readValues(raceDir, "free-practice-4-results.yml", PracticeResult)
                race.qualifying1Results = readValues(raceDir, "qualifying-1-results.yml", QualifyingResult)
                race.qualifying2Results = readValues(raceDir, "qualifying-2-results.yml", QualifyingResult)
                race.qualifyingResults = readValues(raceDir, "qualifying-results.yml", QualifyingResult)
                race.sprintQualifyingStartingGridPositions = readValues(raceDir, "sprint-qualifying-starting-grid-positions.yml", StartingGridPosition)
                race.sprintQualifyingResults = readValues(raceDir, "sprint-qualifying-results.yml", RaceResult)
                race.warmingUpResults = readValues(raceDir, "warming-up-results.yml", PracticeResult)
                race.startingGridPositions = readValues(raceDir, "starting-grid-positions.yml", StartingGridPosition)
                race.raceResults = readValues(raceDir, "race-results.yml", RaceResult)
                race.fastestLaps = readValues(raceDir, "fastest-laps.yml", FastestLap)
                race.pitStops = readValues(raceDir, "pit-stops.yml", PitStop)
                race.driverOfTheDayResults = readValues(raceDir, "driver-of-the-day-results.yml", DriverOfTheDayResult)
                race.driverStandings = readValues(raceDir, "driver-standings.yml", DriverStanding)
                race.constructorStandings = readValues(raceDir, "constructor-standings.yml", ConstructorStanding)

                season.races << race
            }

            // Read standings.

            season.driverStandings = readValues(seasonDir, "driver-standings.yml", DriverStanding)
            season.constructorStandings = readValues(seasonDir, "constructor-standings.yml", ConstructorStanding)

            f1db.seasons << season
        }

        println "Enriching data......."

        f1db.drivers.each { driver ->
            driver.totalChampionshipWins = 0
            driver.totalRaceEntries = 0
            driver.totalRaceStarts = 0
            driver.totalRaceWins = 0
            driver.totalRaceLaps = 0
            driver.totalPodiums = 0
            driver.totalPoints = 0
            driver.totalChampionshipPoints = 0
            driver.totalPolePositions = 0
            driver.totalFastestLaps = 0
            driver.totalDriverOfTheDay = 0
        }

        f1db.constructors.each { constructor ->
            constructor.totalChampionshipWins = 0
            constructor.totalRaceEntries = 0
            constructor.totalRaceStarts = 0
            constructor.totalRaceWins = 0
            constructor.total1And2Finishes = 0
            constructor.totalRaceLaps = 0
            constructor.totalPodiums = 0
            constructor.totalPodiumRaces = 0
            constructor.totalChampionshipPoints = 0
            constructor.totalPolePositions = 0
            constructor.totalFastestLaps = 0
        }

        f1db.engineManufacturers.each { engineManufacturer ->
            engineManufacturer.totalChampionshipWins = 0
            engineManufacturer.totalRaceEntries = 0
            engineManufacturer.totalRaceStarts = 0
            engineManufacturer.totalRaceWins = 0
            engineManufacturer.totalRaceLaps = 0
            engineManufacturer.totalPodiums = 0
            engineManufacturer.totalPodiumRaces = 0
            engineManufacturer.totalChampionshipPoints = 0
            engineManufacturer.totalPolePositions = 0
            engineManufacturer.totalFastestLaps = 0
        }

        f1db.tyreManufacturers.each { tyreManufacturer ->
            tyreManufacturer.totalRaceEntries = 0
            tyreManufacturer.totalRaceStarts = 0
            tyreManufacturer.totalRaceWins = 0
            tyreManufacturer.totalRaceLaps = 0
            tyreManufacturer.totalPodiums = 0
            tyreManufacturer.totalPodiumRaces = 0
            tyreManufacturer.totalPolePositions = 0
            tyreManufacturer.totalFastestLaps = 0
        }

        f1db.seasons.each { season ->

            season.races.each { race ->

                // Enrich sprint qualifying results.

                race.sprintQualifyingResults.each { sprintQualifyingResult ->
                    sprintQualifyingResult.positionsGained = calculatePositionsGained(sprintQualifyingResult, race.sprintQualifyingStartingGridPositions)
                }

                // Enrich race results.

                race.raceResults.each { raceResult ->
                    raceResult.positionsGained = calculatePositionsGained(raceResult, race.startingGridPositions)
                    raceResult.fastestLap = race.fastestLaps?.any { it.positionNumber == 1 && it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                    raceResult.pitStops = race.pitStops?.count { it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                    raceResult.driverOfTheDay = race.driverOfTheDayResults?.any { it.positionNumber == 1 && it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                }

                // Total points.

                ((race.raceResults ?: []) + (race.sprintQualifyingResults ?: [])).each { raceResult ->
                    if (raceResult.points) {
                        def driver = f1db.drivers.find { it.id == raceResult.driverId }
                        driver.totalPoints += raceResult.points
                    }
                }

                def raceEntries = []
                def raceStarts = []
                def raceWins = []
                def podiums = []

                race.startingGridPositions.each { startingGridPosition ->

                    def driver = f1db.drivers.find { it.id == startingGridPosition.driverId }
                    def constructor = f1db.constructors.find { it.id == startingGridPosition.constructorId }
                    def engineManufacturer = f1db.engineManufacturers.find { it.id == startingGridPosition.engineManufacturerId }
                    def tyreManufacturer = f1db.tyreManufacturers.find { it.id == startingGridPosition.tyreManufacturerId }

                    // Best starting grid position.

                    if (startingGridPosition.positionNumber) {
                        driver.bestStartingGridPosition = Math.min(driver.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                        constructor.bestStartingGridPosition = Math.min(constructor.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                        engineManufacturer.bestStartingGridPosition = Math.min(engineManufacturer.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                        tyreManufacturer.bestStartingGridPosition = Math.min(tyreManufacturer.bestStartingGridPosition ?: startingGridPosition.positionNumber, startingGridPosition.positionNumber)
                    }

                    // Total pole positions.

                    if (startingGridPosition.positionNumber == 1) {
                        driver.totalPolePositions++
                        constructor.totalPolePositions++
                        engineManufacturer.totalPolePositions++
                        tyreManufacturer.totalPolePositions++
                    }
                }

                race.raceResults.each { raceResult ->

                    def driver = f1db.drivers.find { it.id == raceResult.driverId }
                    def constructor = f1db.constructors.find { it.id == raceResult.constructorId }
                    def engineManufacturer = f1db.engineManufacturers.find { it.id == raceResult.engineManufacturerId }
                    def tyreManufacturer = f1db.tyreManufacturers.find { it.id == raceResult.tyreManufacturerId }

                    // Best race result.

                    if (raceResult.positionNumber) {
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

                    if (raceResult.positionText !in ["DNP", "DNPQ", "DNQ", "DNS", "EX"]) {
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
                            def position2 = race.raceResults.find { it.positionNumber == 2 }
                            if (position2 && position2.constructorId == constructor.id) {
                                constructor.total1And2Finishes++
                            }
                        }
                    }

                    // Total race laps.

                    if (raceResult.laps) {
                        driver.totalRaceLaps += raceResult.laps
                        if (!raceResult.sharedCar) {
                            constructor.totalRaceLaps += raceResult.laps
                            engineManufacturer.totalRaceLaps += raceResult.laps
                            tyreManufacturer.totalRaceLaps += raceResult.laps
                        }
                    }

                    // Total podiums + podium races.

                    if (raceResult.positionNumber in [1, 2, 3]) {
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

                    if (raceResult.fastestLap) {
                        driver.totalFastestLaps++
                        constructor.totalFastestLaps++
                        engineManufacturer.totalFastestLaps++
                        tyreManufacturer.totalFastestLaps++
                    }

                    // Total driver of the day.

                    if (raceResult.driverOfTheDay) {
                        driver.totalDriverOfTheDay++
                    }
                }
            }

            season.driverStandings?.each { driverStanding ->

                def positionNumber = driverStanding.positionNumber
                def driver = f1db.drivers.find { it.id == driverStanding.driverId }

                // Best championship position.

                if (positionNumber && (season.year < currentSeason || season.races?.last()?.raceResults)) {
                    driver.bestChampionshipPosition = Math.min(driver.bestChampionshipPosition ?: positionNumber, positionNumber)
                }

                // Total championship wins.

                if (positionNumber == 1 && (season.year < currentSeason || wdcDecided)) {
                    driver.totalChampionshipWins++
                }

                // Total championship points.

                driver.totalChampionshipPoints += driverStanding.points
            }

            season.constructorStandings?.each { constructorStanding ->

                def positionNumber = constructorStanding.positionNumber
                def constructor = f1db.constructors.find { it.id == constructorStanding.constructorId }
                def engineManufacturer = f1db.engineManufacturers.find { it.id == constructorStanding.engineManufacturerId }

                // Best championship position.

                if (positionNumber && (season.year < currentSeason || season.races?.last()?.raceResults)) {
                    constructor.bestChampionshipPosition = Math.min(constructor.bestChampionshipPosition ?: positionNumber, positionNumber)
                    engineManufacturer.bestChampionshipPosition = Math.min(engineManufacturer.bestChampionshipPosition ?: positionNumber, positionNumber)
                }

                // Total championship wins.

                if (positionNumber == 1 && (season.year < currentSeason || wccDecided)) {
                    constructor.totalChampionshipWins++
                    engineManufacturer.totalChampionshipWins++
                }

                // Total championship points.

                constructor.totalChampionshipPoints += constructorStanding.points
                engineManufacturer.totalChampionshipPoints += constructorStanding.points
            }
        }

        return f1db
    }

    private Integer calculatePositionsGained(RaceResult raceResult, List<StartingGridPosition> startingGridPositions) {
        def positionsGained = null
        if (raceResult.positionNumber && !raceResult.sharedCar) {
            if (raceResult.gridPositionNumber) {
                positionsGained = raceResult.gridPositionNumber - raceResult.positionNumber
            } else {
                def index = startingGridPositions?.findIndexOf { it.driverNumber == raceResult.driverNumber && it.driverId == raceResult.driverId && it.constructorId == raceResult.constructorId && it.engineManufacturerId == raceResult.engineManufacturerId && it.tyreManufacturerId == raceResult.tyreManufacturerId }
                if (index > -1) {
                    positionsGained = (index + 1) - raceResult.positionNumber
                }
            }
        }
        return positionsGained
    }

    private <T> int incrementIfAbsent(Collection<T> collection, T element) {
        if (collection.contains(element)) {
            return 0
        } else {
            collection << element
            return 1
        }
    }

    private <T> T readValue(File parent, String path, Class<T> valueType) {
        return mapper.readValue(new File(parent, path), valueType)
    }

    private <T> List<T> readValues(File parent, String path, Class<T> valueType) {
        try {
            def data = []
            def src = new File(parent, path)
            if (src.isDirectory()) {
                def files = listFiles(src)
                files.each { file -> data << mapper.readValue(file, valueType) }
            } else {
                def collectionType = mapper.typeFactory.constructCollectionType(List, valueType)
                data = mapper.readValue(src, collectionType)
            }
            return data
        } catch (FileNotFoundException e) {
            return null
        } catch (MismatchedInputException e) {
            if (e.message.startsWith("No content to map due to end-of-input")) {
                return []
            }
            throw e
        }
    }

    private List<File> listFiles(File dir) {
        return dir.listFiles().sort { it.name }
    }

    private List<File> listFiles(File parent, String path) {
        return listFiles(new File(parent, path))
    }

    private ObjectMapper createMapper() {
        def module = new SimpleModule()
        module.addDeserializer(SeasonEntrant, new SeasonEntrantDeserializer())
        module.addDeserializer(SeasonEntrantConstructor, new SeasonEntrantConstructorDeserializer())
        module.setMixInAnnotation(SeasonEntrantDriver, SeasonEntrantDriverMixIn)
        module.setMixInAnnotation(PracticeResult, PracticeResultMixIn)
        module.setMixInAnnotation(QualifyingResult, QualifyingResultMixIn)
        module.setMixInAnnotation(StartingGridPosition, StartingGridPositionMixIn)
        module.setMixInAnnotation(RaceResult, RaceResultMixIn)
        module.setMixInAnnotation(FastestLap, FastestLapMixIn)
        module.setMixInAnnotation(PitStop, PitStopMixIn)
        module.setMixInAnnotation(DriverOfTheDayResult, DriverOfTheDayResultMixIn)
        module.setMixInAnnotation(DriverStanding, DriverStandingMixIn)
        module.setMixInAnnotation(ConstructorStanding, ConstructorStandingMixIn)
        def mapper = new YAMLMapper()
        mapper.propertyNamingStrategy = PropertyNamingStrategies.LOWER_CAMEL_CASE
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
        mapper.registerModules(new JavaTimeModule(), module)
        return mapper
    }

    static Integer toIntegerOrNull(String text) {
        return text?.isInteger() ? text.toInteger() : null
    }

    static Integer toMillis(String time) {
        if (time == null || time.isEmpty() || time == "SHC" || time.matches(/^\+(\d+) lap(s)?$/)) {
            return null
        }
        String str = time
        long hours = 0, minutes = 0, seconds, millis

        // Get millis.
        int index = str.lastIndexOf(".")
        millis = str.substring(index + 1).toLong()
        str = str.substring(0, index)

        // Get seconds.
        index = str.lastIndexOf(":")
        if (index == -1) {
            if (str.startsWith("+")) {
                str = str.substring(1)
            }
            seconds = str.toLong()
            str = ""
        } else {
            seconds = str.substring(index + 1).toLong()
            str = str.substring(0, index)
        }

        // Get minutes.
        if (str.length() > 0) {
            index = str.lastIndexOf(":")
            if (index == -1) {
                if (str.startsWith("+")) {
                    str = str.substring(1)
                }
                minutes = str.toLong()
                str = ""
            } else {
                minutes = str.substring(index + 1).toLong()
                str = str.substring(0, index)
            }
        }

        // Get hours.
        if (str.length() > 0) {
            hours = str.toLong()
        }

        // Return millis.
        return Duration.ofDays(0)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds)
                .plusMillis(millis)
                .toMillis()
                .toInteger()
    }
}
