package com.f1db

import org.junit.jupiter.api.Test
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.sql.DriverManager
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.assertDoesNotThrow

@Testcontainers
class SqlDumpValidationTest {

    companion object {
        @Container
        val mysqlContainer = MySQLContainer("mysql:8.0")
            .withDatabaseName("f1db")
            .withUsername("test")
            .withPassword("test")

        @Container
        val postgresContainer = PostgreSQLContainer("postgres:15")
            .withDatabaseName("f1db")
            .withUsername("test")
            .withPassword("test")
    }

    @Test
    fun `should validate MySQL dump`() {
        val mysqlDumpFile = File("src/main/resources/sql/create_schema.sql")
        assertDoesNotThrow {
            DriverManager.getConnection(
                mysqlContainer.jdbcUrl,
                mysqlContainer.username,
                mysqlContainer.password
            ).use { connection ->
                connection.createStatement().use { statement ->
                    val sqlCommands = mysqlDumpFile.readText().split(";")
                    sqlCommands.forEach { sql ->
                        if (sql.trim().isNotEmpty()) {
                            statement.execute(sql)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `should validate PostgreSQL dump`() {
        val postgresDumpFile = File("src/main/resources/sql/create_schema.sql")
        assertDoesNotThrow {
            DriverManager.getConnection(
                postgresContainer.jdbcUrl,
                postgresContainer.username,
                postgresContainer.password
            ).use { connection ->
                connection.createStatement().use { statement ->
                    val sqlCommands = postgresDumpFile.readText().split(";")
                    sqlCommands.forEach { sql ->
                        if (sql.trim().isNotEmpty()) {
                            statement.execute(sql)
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `should validate SQLite dump`() {
        val sqliteDumpFile = File("src/main/resources/sql/create_schema.sql")
        assertDoesNotThrow {
            // Using in-memory SQLite database for testing
            DriverManager.getConnection("jdbc:sqlite::memory:").use { connection ->
                connection.createStatement().use { statement ->
                    val sqlCommands = sqliteDumpFile.readText().split(";")
                    sqlCommands.forEach { sql ->
                        if (sql.trim().isNotEmpty()) {
                            statement.execute(sql)
                        }
                    }
                }
            }
        }
    }
}
