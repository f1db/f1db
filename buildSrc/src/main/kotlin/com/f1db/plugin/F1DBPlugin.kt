package com.f1db.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The F1DB plugin.
 *
 * @author Marcel Overdijk
 */
abstract class F1DBPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("f1db", F1DBPluginExtension::class.java)
        project.tasks.register("dist", F1DBGenerateDistributionsTask::class.java) {
            it.sourceDir.set(extension.sourceDir)
            it.outputDir.set(extension.outputDir)
            it.schemaDir.set(extension.schemaDir)
            it.currentSeason.set(extension.currentSeason)
        }
    }
}
