package com.f1db.tests.sql

import java.io.File

/**
 * Validates the generated PostgreSQL sql (with single inserts) dump file.
 *
 * @author Marcel Overdijk
 */
class PostgresqlSingleInsertsDumpTest : AbstractPostgresqlDumpTest() {

    override val importFile = File("build/data/sql/f1db-sql-postgresql-single-inserts.sql")
}