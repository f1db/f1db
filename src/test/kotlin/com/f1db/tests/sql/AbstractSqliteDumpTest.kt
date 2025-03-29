package com.f1db.tests.sql

import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import java.io.File
import java.nio.file.Files
import java.sql.DriverManager
import java.util.*

/**
 * Validates the generated SQLite sql dump file.
 *
 * @author Marcel Overdijk
 */
abstract class AbstractSqliteDumpTest : AbstractSqlDumpTest<GenericContainer<Nothing>>() {

    protected abstract val importFile: File

    override fun setupContainer() {

        // Create temporary database file.
        val databaseFile = Files.createTempFile("f1db-", ".db").toFile()
        databaseFile.deleteOnExit()

        // Create the container.
        container = GenericContainer<Nothing>("keinos/sqlite3:latest").apply {
            withCommand("tail", "-f", "/dev/null") // Keeps container running.
            withFileSystemBind(importFile.absolutePath, "/tmp/import.sql", BindMode.READ_ONLY)
            withFileSystemBind(databaseFile.parentFile.absolutePath, "/tmp", BindMode.READ_WRITE)
        }

        // Start the container.
        container.start()

        // Import the dump file.
        container.execAndCheck("sh", "-c", "sqlite3 /tmp/f1db.db < /tmp/import.sql")

        // Create the JDBC connection URL and properties.
        val jdbcUrl = "jdbc:sqlite:${databaseFile.absolutePath}"
        val jdbcProperties = Properties().apply {
            setProperty("date_class", "text")
            setProperty("date_string_format", "yyyy-MM-dd")
        }

        // Create the connection and DSL context.
        connection = DriverManager.getConnection(jdbcUrl, jdbcProperties)
        ctx = DSL.using(connection, SQLDialect.SQLITE)
    }
}
