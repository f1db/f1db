package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.toMillis
import com.f1db.plugin.schema.single.StartingGridPosition
import com.fasterxml.jackson.databind.util.StdConverter

/**
 * The starting grid position converter.
 *
 * @author Marcel Overdijk
 */
class StartingGridPositionConverter : StdConverter<StartingGridPosition, StartingGridPosition>() {

    override fun convert(startingGridPosition: StartingGridPosition): StartingGridPosition {
        startingGridPosition.positionNumber = startingGridPosition.positionText.toIntOrNull()
        startingGridPosition.gridPenaltyPositions = startingGridPosition.gridPenalty?.toIntOrNull()
        startingGridPosition.timeMillis = startingGridPosition.time.toMillis()
        return startingGridPosition
    }
}
