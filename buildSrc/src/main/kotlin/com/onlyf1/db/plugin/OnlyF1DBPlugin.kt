package com.onlyf1.db.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The OnlyF1-DB plugin.
 *
 * @author Marcel Overdijk
 */
abstract class OnlyF1DBPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.create("onlyf1db", OnlyF1DBPluginExtension::class.java)
        project.tasks.register("dist", OnlyF1DBGenerateDistributionsTask::class.java) {
            it.sourceDir.set(extension.sourceDir)
            it.outputDir.set(extension.outputDir)
            it.schemaDir.set(extension.schemaDir)
            it.currentSeason.set(extension.currentSeason)
        }
    }
}
