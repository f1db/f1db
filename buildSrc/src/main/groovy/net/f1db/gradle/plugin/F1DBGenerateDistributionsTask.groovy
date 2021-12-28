package net.f1db.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * The F1DB generate distributions task.
 *
 * @author Marcel Overdijk
 */
class F1DBGenerateDistributionsTask extends DefaultTask {

    @TaskAction
    void generate() {
        def projectDir = project.projectDir
        def extension = project.extensions.f1db
        def f1db = new F1DBReader(projectDir, extension).read()
        new F1DBWriter(projectDir, extension).write(f1db)
    }
}
