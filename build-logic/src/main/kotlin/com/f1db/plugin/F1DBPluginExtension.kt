package com.f1db.plugin

import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

/**
 * The F1DB plugin extension.
 *
 * @author Marcel Overdijk
 */
abstract class F1DBPluginExtension @Inject constructor(objects: ObjectFactory) {

    val sourceDir = objects.directoryProperty()
    val outputDir = objects.directoryProperty()
    val currentSeason = objects.property(Int::class.java)
    val currentSeasonFinished = objects.property(Boolean::class.java)
}
