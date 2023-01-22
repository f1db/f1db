package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.onlyf1.db.schema.single.SeasonEntrant

/**
 * The season entrant deserializer.
 *
 * @author Marcel Overdijk
 */
class SeasonEntrantDeserializer : SeasonEntrantDeserializerSupport<SeasonEntrant>(SeasonEntrant::class.java) {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): SeasonEntrant {
        return readSeasonEntrant(parser, context, parser.readValueAsTree())
    }
}
