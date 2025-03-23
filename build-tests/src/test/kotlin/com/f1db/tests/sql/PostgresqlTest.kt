package com.f1db.tests.sql

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.MountableFile
import java.io.File
import java.sql.Connection

/**
 * Validates the generated PostgreSQL sql dump file.
 *
 * @author Marcel Overdijk
 */
class PostgresqlTest : AbstractSqlTest() {

    companion object {

        private val container = PostgreSQLContainer<Nothing>("postgres:17").apply {
            withCopyFileToContainer(
                MountableFile.forHostPath(File("../build/data/sql/f1db-sql-postgresql.sql").absolutePath),
                "/docker-entrypoint-initdb.d/init.sql")
        }

        private lateinit var connection: Connection
        private lateinit var ctx: DSLContext

        @BeforeAll
        @JvmStatic
        fun setup() {
            container.start()
            connection = container.createConnection("")
            ctx = DSL.using(connection, SQLDialect.POSTGRES)
        }

        @AfterAll
        @JvmStatic
        fun cleanup() {
            connection.close()
            container.stop()
        }
    }

    override fun getDSLContext(): DSLContext = ctx
}