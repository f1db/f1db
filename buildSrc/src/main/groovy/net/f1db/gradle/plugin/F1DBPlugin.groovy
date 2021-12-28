package net.f1db.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * The F1DB plugin.
 *
 * @author Marcel Overdijk
 */
class F1DBPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("f1db", F1DBPluginExtension)
        project.tasks.create("dist", F1DBGenerateDistributionsTask) {
            group = "Generate"
            description = "Generates the F1DB distribution files (json, smile, xml, yaml, sqlite)."
        }
    }
}
