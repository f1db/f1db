package com.f1db.tests.sql

import java.io.File

/**
 * Validates the generated SQLite sql (with single inserts) dump file.
 *
 * @author Marcel Overdijk
 */
class SqliteSingleInsertsDumpTest : AbstractSqliteDumpTest() {

    override val importFile = File("build/data/sql/f1db-sql-sqlite-single-inserts.sql")
}
