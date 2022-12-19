package net.f1db.gradle.plugin.databind.splitted

import com.fasterxml.jackson.annotation.JsonIgnore
import net.f1db.DriverOfTheDayResult
import net.f1db.FastestLap
import net.f1db.PitStop
import net.f1db.PracticeResult
import net.f1db.QualifyingResult
import net.f1db.RaceConstructorStanding
import net.f1db.RaceDriverStanding
import net.f1db.RaceResult
import net.f1db.StartingGridPosition

/**
 * The splitted race mix-in.
 *
 * @author Marcel Overdijk
 */
abstract class SplittedRaceMixIn {

    @JsonIgnore
    List<QualifyingResult> preQualifyingResults

    @JsonIgnore
    List<PracticeResult> freePractice1Results

    @JsonIgnore
    List<PracticeResult> freePractice2Results

    @JsonIgnore
    List<PracticeResult> freePractice3Results

    @JsonIgnore
    List<PracticeResult> freePractice4Results

    @JsonIgnore
    List<QualifyingResult> qualifying1Results

    @JsonIgnore
    List<QualifyingResult> qualifying2Results

    @JsonIgnore
    List<QualifyingResult> qualifyingResults

    @JsonIgnore
    List<StartingGridPosition> sprintQualifyingStartingGridPositions

    @JsonIgnore
    List<RaceResult> sprintQualifyingResults

    @JsonIgnore
    List<PracticeResult> warmingUpResults

    @JsonIgnore
    List<StartingGridPosition> startingGridPositions

    @JsonIgnore
    List<RaceResult> raceResults

    @JsonIgnore
    List<FastestLap> fastestLaps

    @JsonIgnore
    List<PitStop> pitStops

    @JsonIgnore
    List<DriverOfTheDayResult> driverOfTheDayResults

    @JsonIgnore
    List<RaceDriverStanding> driverStandings

    @JsonIgnore
    List<RaceConstructorStanding> constructorStandings

    @JsonIgnore
    abstract List<QualifyingResult> getPreQualifyingResults()

    @JsonIgnore
    abstract List<PracticeResult> getFreePractice1Results()

    @JsonIgnore
    abstract List<PracticeResult> getFreePractice2Results()

    @JsonIgnore
    abstract List<PracticeResult> getFreePractice3Results()

    @JsonIgnore
    abstract List<PracticeResult> getFreePractice4Results()

    @JsonIgnore
    abstract List<QualifyingResult> getQualifying1Results()

    @JsonIgnore
    abstract List<QualifyingResult> getQualifying2Results()

    @JsonIgnore
    abstract List<QualifyingResult> getQualifyingResults()

    @JsonIgnore
    abstract List<StartingGridPosition> getSprintQualifyingStartingGridPositions()

    @JsonIgnore
    abstract List<RaceResult> getSprintQualifyingResults()

    @JsonIgnore
    abstract List<PracticeResult> getWarmingUpResults()

    @JsonIgnore
    abstract List<StartingGridPosition> getStartingGridPositions()

    @JsonIgnore
    abstract List<RaceResult> getRaceResults()

    @JsonIgnore
    abstract List<FastestLap> getFastestLaps()

    @JsonIgnore
    abstract List<PitStop> getPitStops()

    @JsonIgnore
    abstract List<DriverOfTheDayResult> getDriverOfTheDayResults()

    @JsonIgnore
    abstract List<RaceDriverStanding> getDriverStandings()

    @JsonIgnore
    abstract List<RaceConstructorStanding> getConstructorStandings()
}
