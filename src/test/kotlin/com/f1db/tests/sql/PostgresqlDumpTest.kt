package com.f1db.tests.sql

import java.io.File

/**
 * Validates the generated PostgreSQL sql dump file.
 *
 * @author Marcel Overdijk
 */
class PostgresqlDumpTest : AbstractPostgresqlDumpTest() {

    override val importFile = File("build/data/sql/f1db-sql-postgresql.sql")
}