package com.f1db.tests.sql

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.MountableFile
import java.io.File
import java.sql.Connection

/**
 * Validates the generated MySQL sql dump file.
 *
 * @author Marcel Overdijk
 */
class MysqlTest : AbstractSqlTest() {

    companion object {

        private val container = MySQLContainer<Nothing>("mysql:8").apply {
            withEnv("LANG", "C.UTF-8")
            withCommand("--max_allowed_packet=128M")
            withCopyFileToContainer(
                MountableFile.forHostPath(File("build/data/sql/f1db-sql-mysql.sql").absolutePath),
                "/docker-entrypoint-initdb.d/init.sql")
        }

        private lateinit var connection: Connection
        private lateinit var ctx: DSLContext

        @BeforeAll
        @JvmStatic
        fun setup() {
            container.start()
            connection = container.createConnection("")
            ctx = DSL.using(connection, SQLDialect.MYSQL)
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
