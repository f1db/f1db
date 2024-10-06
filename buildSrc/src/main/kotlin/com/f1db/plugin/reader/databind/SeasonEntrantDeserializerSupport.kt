package com.f1db.plugin.reader.databind

import com.f1db.plugin.extensions.asText
import com.f1db.plugin.extensions.contains
import com.f1db.plugin.extensions.toRounds
import com.f1db.plugin.schema.single.SeasonEntrant
import com.f1db.plugin.schema.single.SeasonEntrantChassis
import com.f1db.plugin.schema.single.SeasonEntrantConstructor
import com.f1db.plugin.schema.single.SeasonEntrantDriver
import com.f1db.plugin.schema.single.SeasonEntrantEngine
import com.f1db.plugin.schema.single.SeasonEntrantTyreManufacturer
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

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
 *   chassisId:
 *   engineManufacturerId:
 *   engineId:
 *   tyreManufacturerId:
 *   driverId:
 *   rounds:
 *
 * - entrantId:
 *   countryId:
 *   constructorId:
 *   chassis:
 *     - chassisId:
 *     - chassisId:
 *   engineManufacturerId:
 *   engines:
 *     - engineId:
 *     - engineId:
 *   tyreManufacturerId:
 *   driverId:
 *   rounds:
 *
 * - entrantId:
 *   countryId:
 *   constructorId:
 *   chassisId:
 *   engineManufacturerId:
 *   engineId:
 *   tyreManufacturers:
 *     - tyreManufacturerId:
 *     - tyreManufacturerId:
 *   driverId:
 *   rounds:
 *
 * - entrantId:
 *   countryId:
 *   constructorId:
 *   chassisId:
 *   engineManufacturerId:
 *   engineId:
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
 *       chassis:
 *         - chassisId:
 *         - chassisId:
 *       engineManufacturerId:
 *       engines:
 *         - engineId:
 *         - engineId:
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
abstract class SeasonEntrantDeserializerSupport<T>(vc: Class<*>) : StdDeserializer<T>(vc) {

    protected fun readSeasonEntrant(parser: JsonParser, context: DeserializationContext, node: TreeNode): SeasonEntrant {
        val seasonEntrant = SeasonEntrant()
        seasonEntrant.entrantId = node.asText("entrantId")
        seasonEntrant.countryId = node.asText("countryId")
        seasonEntrant.constructors = mutableListOf()
        if (node.contains("constructors")) {
            seasonEntrant.constructors.addAll(readValues<SeasonEntrantConstructor>(parser, context, node, "constructors"))
        }
        if (node.contains("constructorId")) {
            seasonEntrant.constructors.add(readSeasonEntrantConstructor(parser, context, node))
        }
        return seasonEntrant
    }

    protected fun readSeasonEntrantConstructor(parser: JsonParser, context: DeserializationContext, node: TreeNode): SeasonEntrantConstructor {
        val seasonEntrantConstructor = SeasonEntrantConstructor()
        seasonEntrantConstructor.constructorId = node.asText("constructorId")
        seasonEntrantConstructor.chassis = mutableListOf()
        seasonEntrantConstructor.engineManufacturerId = node.asText("engineManufacturerId")
        seasonEntrantConstructor.engines = mutableListOf()
        seasonEntrantConstructor.tyreManufacturers = mutableListOf()
        seasonEntrantConstructor.drivers = mutableListOf()
        if (node.contains("chassis")) {
            seasonEntrantConstructor.chassis.addAll(readValues(parser, context, node, "chassis"))
        }
        if (node.contains("chassisId")) {
            val seasonEntrantChassis = SeasonEntrantChassis()
            seasonEntrantChassis.chassisId = node.asText("chassisId")
            seasonEntrantConstructor.chassis.add(seasonEntrantChassis)
        }
        if (node.contains("engines")) {
            seasonEntrantConstructor.engines.addAll(readValues(parser, context, node, "engines"))
        }
        if (node.contains("engineId")) {
            val seasonEntrantEngine = SeasonEntrantEngine()
            seasonEntrantEngine.engineId = node.asText("engineId")
            seasonEntrantConstructor.engines.add(seasonEntrantEngine)
        }
        if (node.contains("tyreManufacturers")) {
            seasonEntrantConstructor.tyreManufacturers.addAll(readValues(parser, context, node, "tyreManufacturers"))
        }
        if (node.contains("tyreManufacturerId")) {
            val seasonEntrantTyreManufacturer = SeasonEntrantTyreManufacturer()
            seasonEntrantTyreManufacturer.tyreManufacturerId = node.asText("tyreManufacturerId")
            seasonEntrantConstructor.tyreManufacturers.add(seasonEntrantTyreManufacturer)
        }
        if (node.contains("drivers")) {
            seasonEntrantConstructor.drivers.addAll(readValues(parser, context, node, "drivers"))
        }
        if (node.contains("driverId")) {
            val seasonEntrantDriver = SeasonEntrantDriver()
            seasonEntrantDriver.driverId = node.asText("driverId")
            seasonEntrantDriver.roundsText = node.asText("rounds")
            seasonEntrantDriver.rounds = seasonEntrantDriver.roundsText.toRounds()
            seasonEntrantDriver.testDriver = node.asText("testDriver")?.toBoolean() ?: false
            seasonEntrantConstructor.drivers.add(seasonEntrantDriver)
        }
        return seasonEntrantConstructor
    }

    protected inline fun <reified RT> readValues(parser: JsonParser, context: DeserializationContext, node: TreeNode, fieldName: String): List<RT> {
        val collectionType = context.typeFactory.constructCollectionType(List::class.java, RT::class.java)
        val tokens = parser.codec.treeAsTokens(node.get(fieldName))
        return parser.codec.readValue(tokens, collectionType)
    }
}
