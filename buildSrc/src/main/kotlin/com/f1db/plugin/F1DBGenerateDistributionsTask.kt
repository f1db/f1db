package com.f1db.plugin

import com.f1db.plugin.reader.F1DBReader
import com.f1db.plugin.writer.F1DBWriter
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

/**
 * The F1DB generate distributions task.
 *
 * @author Marcel Overdijk
 */
abstract class F1DBGenerateDistributionsTask : DefaultTask() {

    init {
        group = "Generate"
        description = "Generates the F1DB distribution files (csv, json, smile, sql, sqlite)."
    }

    @get:Internal
    abstract val sourceDir: DirectoryProperty

    @get:Internal
    abstract val outputDir: DirectoryProperty

    @get:Internal
    abstract val schemaDir: DirectoryProperty

    @get:Internal
    abstract val currentSeason: Property<CurrentSeason>

    @get:Input
    val formats: ListProperty<String> = project.objects.listProperty(String::class.java)

    @Option(option = "formats", description = "Configures the output formats.")
    fun setFormats(formats: String) {
        this.formats.set(formats.split(",").map { it.trim() })
    }

    @TaskAction
    fun generate() {
        val db = F1DBReader(sourceDir.get().asFile, currentSeason.get()).read()
        F1DBWriter(project.name, outputDir.get().asFile, schemaDir.get().asFile, formats.get(), db).write()
    }
}
