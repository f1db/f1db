package com.f1db.tests.sqlite

import com.f1db.tests.sql.AbstractSqlTest
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.util.*

/**
 * Validates the generated SQLite database.
 *
 * @author Marcel Overdijk
 */
class SqliteDatabaseTest : AbstractSqlTest() {

    private lateinit var connection: Connection

    @BeforeAll
    fun setup() {
        val file = File("build/data/sqlite/f1db.db")
        val jdbcUrl = "jdbc:sqlite:${file.absolutePath}"
        val jdbcProperties = Properties().apply {
            setProperty("date_class", "text")
            setProperty("date_string_format", "yyyy-MM-dd")
        }
        connection = DriverManager.getConnection(jdbcUrl, jdbcProperties)
        ctx = DSL.using(connection, SQLDialect.SQLITE)
    }

    @AfterAll
    fun cleanup() {
        connection.close()
    }
}