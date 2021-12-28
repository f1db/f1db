package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.databind.util.StdConverter
import net.f1db.StartingGridPosition

import static net.f1db.gradle.plugin.F1DBReader.toIntegerOrNull
import static net.f1db.gradle.plugin.F1DBReader.toMillis

/**
 * The starting grid position converter.
 *
 * @author Marcel Overdijk
 */
class StartingGridPositionConverter extends StdConverter<StartingGridPosition, StartingGridPosition> {

    @Override
    StartingGridPosition convert(StartingGridPosition startingGridPosition) {
        startingGridPosition.positionNumber = toIntegerOrNull startingGridPosition.positionText
        startingGridPosition.gridPenaltyPositions = toIntegerOrNull startingGridPosition.gridPenalty
        startingGridPosition.timeMillis = toMillis startingGridPosition.time
        return startingGridPosition
    }
}
