package com.f1db.tests.sql

import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.PostgreSQLContainer
import java.io.File

/**
 * Validates the generated PostgreSQL sql dump file.
 *
 * @author Marcel Overdijk
 */
abstract class AbstractPostgresqlDumpTest : AbstractSqlDumpTest<PostgreSQLContainer<Nothing>>() {

    protected abstract val importFile: File

    override fun setupContainer() {

        // Create the container.
        container = PostgreSQLContainer<Nothing>("postgres:17").apply {
            withFileSystemBind(importFile.absolutePath, "/tmp/import.sql", BindMode.READ_ONLY)
        }

        // Start the container.
        container.start()

        // Import the dump file.
        container.execAndCheck("bash", "-c", "psql -U ${container.username} -d ${container.databaseName} -f /tmp/import.sql")

        // Create the connection and DSL context.
        connection = container.createConnection("")
        ctx = DSL.using(connection, SQLDialect.POSTGRES)
    }
}
