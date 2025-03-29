package com.f1db.tests.sql

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.GenericContainer
import java.sql.Connection

/**
 * Abstract class for validating a sql dump file.
 *
 * @author Marcel Overdijk
 */
abstract class AbstractSqlDumpTest<T : GenericContainer<*>> : AbstractSqlTest() {

    protected lateinit var container: T
    protected lateinit var connection: Connection

    abstract fun setupContainer()

    @BeforeAll
    fun setup() {
        setupContainer()
    }

    @AfterAll
    fun cleanup() {
        connection.close()
        container.stop()
    }

    fun GenericContainer<*>.execAndCheck(vararg command: String) {
        val result = execInContainer(*command)
        check(result.exitCode == 0) {
            """
            |Command failed: ${command.joinToString(" ")}
            |Exit code: ${result.exitCode}
            |STDOUT:
            |${result.stdout}
            |STDERR:
            |${result.stderr}
            """.trimMargin()
        }
    }
}
