package com.f1db.tests.sql

import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.MySQLContainer
import java.io.File

/**
 * Validates the generated MySQL sql dump file.
 *
 * @author Marcel Overdijk
 */
abstract class AbstractMysqlDumpTest : AbstractSqlDumpTest<MySQLContainer<Nothing>>() {

    protected abstract val importFile: File

    override fun setupContainer() {

        // Create the container.
        container = MySQLContainer<Nothing>("mysql:8").apply {
            withEnv("LANG", "C.UTF-8")
            withCommand(
                "--max_allowed_packet=128M",          // 💡 Increase the maximum allowed packet size to handle large SQL statements.
                "--innodb_flush_log_at_trx_commit=0", // 🚀 Don't flush to disk on every transaction commit (unsafe but much faster for bulk imports).
                "--innodb_doublewrite=0",             // 🚀 Disable the doublewrite buffer, reducing I/O (less crash-safe but faster).
                "--skip-log-bin",                     // 🔥 Disable binary logging (improves import speed).
            )
            withFileSystemBind(importFile.absolutePath, "/tmp/import.sql", BindMode.READ_ONLY)
        }

        // Start the container.
        container.start()

        // Import the dump file.
        container.execAndCheck("bash", "-c", "mysql -u${container.username} -p${container.password} ${container.databaseName} < /tmp/import.sql")

        // Create the connection and DSL context.
        connection = container.createConnection("")
        ctx = DSL.using(connection, SQLDialect.MYSQL)
    }
}
