package com.f1db.plugin.reader.databind

import com.f1db.schema.single.SeasonEntrantConstructor
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext

/**
 * The season entrant constructor deserializer.
 *
 * @author Marcel Overdijk
 */
class SeasonEntrantConstructorDeserializer : SeasonEntrantDeserializerSupport<SeasonEntrantConstructor>(SeasonEntrantConstructor::class.java) {

    override fun deserialize(parser: JsonParser, context: DeserializationContext): SeasonEntrantConstructor {
        return readSeasonEntrantConstructor(parser, context, parser.readValueAsTree())
    }
}
