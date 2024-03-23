package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.RaceDataType
import com.f1db.plugin.schema.splitted.RaceResult
import com.f1db.plugin.writer.sql.tables.records.RaceDataRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The race result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface RaceRaceResultMapper : RecordUnmapper<RaceResult, RaceDataRecord> {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "practiceTime", ignore = true)
    @Mapping(target = "practiceTimeMillis", ignore = true)
    @Mapping(target = "practiceGap", ignore = true)
    @Mapping(target = "practiceGapMillis", ignore = true)
    @Mapping(target = "practiceInterval", ignore = true)
    @Mapping(target = "practiceIntervalMillis", ignore = true)
    @Mapping(target = "practiceLaps", ignore = true)
    @Mapping(target = "qualifyingTime", ignore = true)
    @Mapping(target = "qualifyingTimeMillis", ignore = true)
    @Mapping(target = "qualifyingQ1", ignore = true)
    @Mapping(target = "qualifyingQ1Millis", ignore = true)
    @Mapping(target = "qualifyingQ2", ignore = true)
    @Mapping(target = "qualifyingQ2Millis", ignore = true)
    @Mapping(target = "qualifyingQ3", ignore = true)
    @Mapping(target = "qualifyingQ3Millis", ignore = true)
    @Mapping(target = "qualifyingGap", ignore = true)
    @Mapping(target = "qualifyingGapMillis", ignore = true)
    @Mapping(target = "qualifyingInterval", ignore = true)
    @Mapping(target = "qualifyingIntervalMillis", ignore = true)
    @Mapping(target = "qualifyingLaps", ignore = true)
    @Mapping(target = "startingGridPositionGridPenalty", ignore = true)
    @Mapping(target = "startingGridPositionGridPenaltyPositions", ignore = true)
    @Mapping(target = "startingGridPositionTime", ignore = true)
    @Mapping(target = "startingGridPositionTimeMillis", ignore = true)
    @Mapping(target = "raceSharedCar", source = "sharedCar")
    @Mapping(target = "raceLaps", source = "laps")
    @Mapping(target = "raceTime", source = "time")
    @Mapping(target = "raceTimeMillis", source = "timeMillis")
    @Mapping(target = "raceTimePenalty", source = "timePenalty")
    @Mapping(target = "raceTimePenaltyMillis", source = "timePenaltyMillis")
    @Mapping(target = "raceGap", source = "gap")
    @Mapping(target = "raceGapMillis", source = "gapMillis")
    @Mapping(target = "raceGapLaps", source = "gapLaps")
    @Mapping(target = "raceInterval", source = "interval")
    @Mapping(target = "raceIntervalMillis", source = "intervalMillis")
    @Mapping(target = "raceReasonRetired", source = "reasonRetired")
    @Mapping(target = "racePoints", source = "points")
    @Mapping(target = "raceGridPositionNumber", source = "gridPositionNumber")
    @Mapping(target = "raceGridPositionText", source = "gridPositionText")
    @Mapping(target = "racePositionsGained", source = "positionsGained")
    @Mapping(target = "racePitStops", source = "pitStops")
    @Mapping(target = "raceFastestLap", source = "fastestLap")
    @Mapping(target = "raceDriverOfTheDay", source = "driverOfTheDay")
    @Mapping(target = "raceGrandSlam", source = "grandSlam")
    @Mapping(target = "fastestLapLap", ignore = true)
    @Mapping(target = "fastestLapTime", ignore = true)
    @Mapping(target = "fastestLapTimeMillis", ignore = true)
    @Mapping(target = "fastestLapGap", ignore = true)
    @Mapping(target = "fastestLapGapMillis", ignore = true)
    @Mapping(target = "fastestLapInterval", ignore = true)
    @Mapping(target = "fastestLapIntervalMillis", ignore = true)
    @Mapping(target = "pitStopStop", ignore = true)
    @Mapping(target = "pitStopLap", ignore = true)
    @Mapping(target = "pitStopTime", ignore = true)
    @Mapping(target = "pitStopTimeMillis", ignore = true)
    @Mapping(target = "driverOfTheDayPercentage", ignore = true)
    override fun unmap(source: RaceResult): RaceDataRecord

    fun unmap(source: RaceResult, type: RaceDataType): RaceDataRecord {
        return unmap(source).apply { setType(type.name) }
    }

    fun unmap(sources: List<RaceResult>, type: RaceDataType): List<RaceDataRecord> {
        return sources.map { unmap(it, type) }
    }
}
