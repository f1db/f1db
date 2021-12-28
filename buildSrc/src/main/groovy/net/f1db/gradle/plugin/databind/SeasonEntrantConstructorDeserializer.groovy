package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import net.f1db.SeasonEntrantConstructor

/**
 * The season entrant constructor deserializer.
 *
 * @author Marcel Overdijk
 */
class SeasonEntrantConstructorDeserializer extends SeasonEntrantDeserializerSupport<SeasonEntrantConstructor> {

    SeasonEntrantConstructorDeserializer() {
        super(SeasonEntrantConstructor)
    }

    @Override
    SeasonEntrantConstructor deserialize(JsonParser parser, DeserializationContext context) {
        return readSeasonEntrantConstructor(parser, context, parser.readValueAsTree())
    }
}
