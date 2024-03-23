package com.f1db.plugin.reader.databind

import com.f1db.plugin.schema.single.SeasonEntrant
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext

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
