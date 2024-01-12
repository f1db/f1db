package com.f1db.plugin

import com.f1db.plugin.reader.F1DBReader
import com.f1db.plugin.writer.F1DBWriter
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

/**
 * The F1DB generate distributions task.
 *
 * @author Marcel Overdijk
 */
abstract class F1DBGenerateDistributionsTask : DefaultTask() {

    init {
        group = "Generate"
        description = "Generates the F1DB distribution files (csv, json, smile, sqlite)."
    }

    @get:Internal
    abstract val sourceDir: DirectoryProperty

    @get:Internal
    abstract val outputDir: DirectoryProperty

    @get:Internal
    abstract val schemaDir: DirectoryProperty

    @get:Internal
    abstract val currentSeason: Property<CurrentSeason>

    @TaskAction
    fun generate() {
        val db = F1DBReader(sourceDir.get().asFile, currentSeason.get()).read()
        F1DBWriter(project.name, outputDir.get().asFile, schemaDir.get().asFile, db).write()
    }
}
