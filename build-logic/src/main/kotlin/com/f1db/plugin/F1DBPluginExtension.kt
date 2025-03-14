package com.f1db.plugin

import org.gradle.api.Action
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
    val schemaDir = objects.directoryProperty()
    val currentSeason = objects.newInstance(CurrentSeason::class.java, objects)

    fun currentSeason(action: Action<CurrentSeason>) {
        action.execute(currentSeason)
    }
}

abstract class CurrentSeason @Inject constructor(objects: ObjectFactory) {

    val year = objects.property(Int::class.java)
    val finished = objects.property(Boolean::class.java)
}
