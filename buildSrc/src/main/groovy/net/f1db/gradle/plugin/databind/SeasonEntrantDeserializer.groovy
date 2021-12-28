package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import net.f1db.SeasonEntrant

/**
 * The season entrant deserializer.
 *
 * @author Marcel Overdijk
 */
class SeasonEntrantDeserializer extends SeasonEntrantDeserializerSupport<SeasonEntrant> {

    SeasonEntrantDeserializer() {
        super(SeasonEntrant)
    }

    @Override
    SeasonEntrant deserialize(JsonParser parser, DeserializationContext context) {
        return readSeasonEntrant(parser, context, parser.readValueAsTree())
    }
}
