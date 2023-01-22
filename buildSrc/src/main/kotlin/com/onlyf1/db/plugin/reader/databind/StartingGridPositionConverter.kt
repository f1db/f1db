package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.databind.util.StdConverter
import com.onlyf1.db.plugin.extensions.toMillis
import com.onlyf1.db.schema.single.StartingGridPosition

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
