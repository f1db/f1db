package com.f1db.tests.sql

import java.io.File

/**
 * Validates the generated SQLite sql dump file.
 *
 * @author Marcel Overdijk
 */
class SqliteDumpTest : AbstractSqliteDumpTest() {

    override val importFile = File("build/data/sql/f1db-sql-sqlite.sql")
}
