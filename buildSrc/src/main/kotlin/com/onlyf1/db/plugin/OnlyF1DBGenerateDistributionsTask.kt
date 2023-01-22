package com.onlyf1.db.plugin

import com.onlyf1.db.plugin.reader.OnlyF1DBReader
import com.onlyf1.db.plugin.writer.OnlyF1DBWriter
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

/**
 * The OnlyF1-DB generate distributions task.
 *
 * @author Marcel Overdijk
 */
abstract class OnlyF1DBGenerateDistributionsTask : DefaultTask() {

    init {
        group = "Generate"
        description = "Generates the OnlyF1-DB distribution files (csv, json, smile, sqlite)."
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
        val db = OnlyF1DBReader(sourceDir.get().asFile, currentSeason.get()).read()
        OnlyF1DBWriter(project.name, outputDir.get().asFile, schemaDir.get().asFile, db).write()
    }
}
