package com.f1db.plugin.writer

import com.f1db.plugin.schema.single.F1db
import com.f1db.plugin.writer.csv.CsvWriter
import com.f1db.plugin.writer.json.JsonWriter
import com.f1db.plugin.writer.smile.SmileWriter
import com.f1db.plugin.writer.sql.SqlWriter
import com.f1db.plugin.writer.sqlite.SqliteWriter
import java.io.File

/**
 * The F1DB writer.
 *
 * @author Marcel Overdijk
 */
class F1DBWriter(
        private val projectName: String,
        private val outputDir: File,
        private val schemaDir: File,
        private val formats: List<String>,
        private val db: F1db,
) {

    fun write() {

        println("Writing data........")

        outputDir.deleteRecursively()
        outputDir.mkdirs()

        val writers = mapOf(
                "csv" to { CsvWriter(projectName, File(outputDir, "csv"), db).write() },
                "json" to { JsonWriter(projectName, File(outputDir, "json"), schemaDir, db).write() },
                "smile" to { SmileWriter(projectName, File(outputDir, "smile"), schemaDir, db).write() },
                "sql" to { SqlWriter(projectName, File(outputDir, "sql"), db).write() },
                "sqlite" to { SqliteWriter(projectName, File(outputDir, "sqlite"), db).write() }
        )

        if (formats.isEmpty()) {
            writers.values.forEach { it() }
        } else {
            formats.forEach { format ->
                writers[format]?.invoke() ?: throw IllegalArgumentException("Unsupported format: $format")
            }
        }
    }
}
