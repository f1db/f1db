package com.f1db.plugin.writer

import com.f1db.plugin.schema.single.F1db
import com.f1db.plugin.writer.csv.CsvWriter
import com.f1db.plugin.writer.json.JsonWriter
import com.f1db.plugin.writer.smile.SmileWriter
import com.f1db.plugin.writer.sql.SqlDumpWriter
import com.f1db.plugin.writer.sqlite.SqliteDatabaseWriter
import java.io.File

/**
 * The F1DB writer.
 *
 * @author Marcel Overdijk
 */
class F1DBWriter(
        private val outputDir: File,
        private val formats: List<String>,
        private val db: F1db,
) {

    fun write() {

        println("Writing data........")

        outputDir.deleteRecursively()
        outputDir.mkdirs()

        val writers = mapOf(
                "csv" to { CsvWriter(File(outputDir, "csv"), db).write() },
                "json" to { JsonWriter(File(outputDir, "json"), db).write() },
                "smile" to { SmileWriter(File(outputDir, "smile"), db).write() },
                "sql" to { SqlDumpWriter(File(outputDir, "sql"), db).write() },
                "sqlite" to { SqliteDatabaseWriter(File(outputDir, "sqlite"), db).write() }
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
