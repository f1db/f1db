package com.f1db.test.sqlite

import com.f1db.test.sql.AbstractSqlTest
import org.jooq.DSLContext
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
class SqliteTest : AbstractSqlTest() {

    companion object {

        private lateinit var connection: Connection
        private lateinit var ctx: DSLContext

        @BeforeAll
        @JvmStatic
        fun setup() {
            val file = File("../build/data/sqlite/f1db.db")
            val jdbcUrl = "jdbc:sqlite:${file.absolutePath}"
            val jdbcProperties = Properties().apply {
                setProperty("date_class", "text")
                setProperty("date_string_format", "yyyy-MM-dd")
            }
            connection = DriverManager.getConnection(jdbcUrl, jdbcProperties)
            ctx = DSL.using(connection, SQLDialect.SQLITE)
        }

        @AfterAll
        @JvmStatic
        fun cleanup() {
            connection.close()
        }
    }

    override fun getDSLContext(): DSLContext = ctx
}