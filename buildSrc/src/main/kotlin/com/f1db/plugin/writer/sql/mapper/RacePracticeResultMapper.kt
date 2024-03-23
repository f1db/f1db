package com.f1db.plugin.writer.sql.mapper

import com.f1db.plugin.schema.RaceDataType
import com.f1db.plugin.schema.splitted.PracticeResult
import com.f1db.plugin.writer.sql.tables.records.RaceDataRecord
import org.jooq.RecordUnmapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.ReportingPolicy

/**
 * The practice result mapper.
 *
 * @author Marcel Overdijk
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface RacePracticeResultMapper : RecordUnmapper<PracticeResult, RaceDataRecord> {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "practiceTime", source = "time")
    @Mapping(target = "practiceTimeMillis", source = "timeMillis")
    @Mapping(target = "practiceGap", source = "gap")
    @Mapping(target = "practiceGapMillis", source = "gapMillis")
    @Mapping(target = "practiceInterval", source = "interval")
    @Mapping(target = "practiceIntervalMillis", source = "intervalMillis")
    @Mapping(target = "practiceLaps", source = "laps")
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
    @Mapping(target = "raceSharedCar", ignore = true)
    @Mapping(target = "raceLaps", ignore = true)
    @Mapping(target = "raceTime", ignore = true)
    @Mapping(target = "raceTimeMillis", ignore = true)
    @Mapping(target = "raceTimePenalty", ignore = true)
    @Mapping(target = "raceTimePenaltyMillis", ignore = true)
    @Mapping(target = "raceGap", ignore = true)
    @Mapping(target = "raceGapMillis", ignore = true)
    @Mapping(target = "raceGapLaps", ignore = true)
    @Mapping(target = "raceInterval", ignore = true)
    @Mapping(target = "raceIntervalMillis", ignore = true)
    @Mapping(target = "raceReasonRetired", ignore = true)
    @Mapping(target = "racePoints", ignore = true)
    @Mapping(target = "raceGridPositionNumber", ignore = true)
    @Mapping(target = "raceGridPositionText", ignore = true)
    @Mapping(target = "racePositionsGained", ignore = true)
    @Mapping(target = "racePitStops", ignore = true)
    @Mapping(target = "raceFastestLap", ignore = true)
    @Mapping(target = "raceDriverOfTheDay", ignore = true)
    @Mapping(target = "raceGrandSlam", ignore = true)
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
    override fun unmap(source: PracticeResult): RaceDataRecord

    fun unmap(source: PracticeResult, type: RaceDataType): RaceDataRecord {
        return unmap(source).apply { setType(type.name) }
    }

    fun unmap(sources: List<PracticeResult>, type: RaceDataType): List<RaceDataRecord> {
        return sources.map { unmap(it, type) }
    }
}
