package net.f1db.gradle.plugin.databind

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.NumericNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.type.CollectionType
import net.f1db.SeasonEntrant
import net.f1db.SeasonEntrantConstructor
import net.f1db.SeasonEntrantDriver
import net.f1db.SeasonEntrantTyreManufacturer

/**
 * The season entrant deserializer support class.
 * <p>
 * Supports parsing the following formats (or combinations of them):
 *
 * <pre>{@code
 *
 * - entrantId:
 *   countryId:
 *   constructorId:
 *   engineManufacturerId:
 *   tyreManufacturerId:
 *   driverId:
 *   rounds:
 *
 * - entrantId:
 *   countryId:
 *   constructorId:
 *   engineManufacturerId:
 *   tyreManufacturers:
 *     - tyreManufacturerId:
 *     - tyreManufacturerId:
 *   driverId:
 *   rounds:
 *
 * - entrantId:
 *   countryId:
 *   constructorId:
 *   engineManufacturerId:
 *   tyreManufacturerId:
 *   drivers:
 *     - driverId:
 *       rounds:
 *     - driverId:
 *       rounds:
 *
 * - entrantId:
 *   countryId:
 *   constructors
 *     - constructorId:
 *       engineManufacturerId:
 *       tyreManufacturerId:
 *       drivers:
 *         - driverId:
 *           rounds:
 *         - driverId:
 *           rounds:
 *}</pre>
 *
 * @author Marcel Overdijk
 */
abstract class SeasonEntrantDeserializerSupport<T> extends StdDeserializer<T> {

    SeasonEntrantDeserializerSupport(Class<?> vc) {
        super(vc)
    }

    protected SeasonEntrant readSeasonEntrant(JsonParser parser, DeserializationContext context, TreeNode node) {
        def seasonEntrant = new SeasonEntrant()
        seasonEntrant.entrantId = getText(node, "entrantId")
        seasonEntrant.countryId = getText(node, "countryId")
        seasonEntrant.constructors = []
        if (contains(node, "constructors")) {
            seasonEntrant.constructors.addAll(readValues(parser, context, node, "constructors", SeasonEntrantConstructor))
        }
        if (contains(node, "constructorId")) {
            seasonEntrant.constructors.add(readSeasonEntrantConstructor(parser, context, node))
        }
        return seasonEntrant
    }

    protected SeasonEntrantConstructor readSeasonEntrantConstructor(JsonParser parser, DeserializationContext context, TreeNode node) {
        def seasonEntrantConstructor = new SeasonEntrantConstructor()
        seasonEntrantConstructor.constructorId = getText(node, "constructorId")
        seasonEntrantConstructor.engineManufacturerId = getText(node, "engineManufacturerId")
        seasonEntrantConstructor.tyreManufacturers = []
        if (contains(node, "tyreManufacturers")) {
            seasonEntrantConstructor.tyreManufacturers.addAll(readValues(parser, context, node, "tyreManufacturers", SeasonEntrantTyreManufacturer))
        }
        if (contains(node, "tyreManufacturerId")) {
            def seasonEntrantTyreManufacturer = new SeasonEntrantTyreManufacturer()
            seasonEntrantTyreManufacturer.tyreManufacturerId = getText(node, "tyreManufacturerId")
            seasonEntrantConstructor.tyreManufacturers.add(seasonEntrantTyreManufacturer)
        }
        seasonEntrantConstructor.drivers = []
        if (contains(node, "drivers")) {
            seasonEntrantConstructor.drivers.addAll(readValues(parser, context, node, "drivers", SeasonEntrantDriver))
        }
        if (contains(node, "driverId")) {
            def seasonEntrantDriver = new SeasonEntrantDriver()
            seasonEntrantDriver.driverId = getText(node, "driverId")
            seasonEntrantDriver.roundsText = getText(node, "rounds")
            seasonEntrantDriver.rounds = SeasonEntrantDriverConverter.toRounds(seasonEntrantDriver.roundsText)
            seasonEntrantDriver.testDriver = getText(node, "testDriver")?.toBoolean() ?: false
            seasonEntrantConstructor.drivers.add(seasonEntrantDriver)
        }
        return seasonEntrantConstructor
    }

    protected boolean contains(TreeNode node, String fieldName) {
        return node.fieldNames().toList().contains(fieldName)
    }

    protected String getText(TreeNode node, String fieldName) {
        TreeNode fieldNode = node.get(fieldName)
        switch (fieldNode) {
            case null:
                return null
            case NullNode:
                return null
            case BooleanNode:
                return (fieldNode as BooleanNode).asText()
            case NumericNode:
                return (fieldNode as NumericNode).asText()
            case TextNode:
                return (fieldNode as TextNode).asText()
            default:
                throw new IllegalArgumentException("Cannot get text for field: ${fieldName}")
        }
    }

    protected <RT> List<RT> readValues(JsonParser parser, DeserializationContext context, TreeNode node, String fieldName, Class<? extends RT> elementClass) {
        JsonParser tokens = parser.codec.treeAsTokens(node.get(fieldName))
        CollectionType collectionType = context.typeFactory.constructCollectionType(ArrayList.class, elementClass)
        return parser.codec.readValue(tokens, collectionType)
    }
}
