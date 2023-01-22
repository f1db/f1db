package com.onlyf1.db.plugin.reader.databind

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.onlyf1.db.schema.single.SeasonEntrantConstructor

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
