package com.f1db.tests.sql

import java.io.File

/**
 * Validates the generated MySQL sql (with single inserts) dump file.
 *
 * @author Marcel Overdijk
 */
class MysqlSingleInsertsDumpTest : AbstractMysqlDumpTest() {

    override val importFile = File("build/data/sql/f1db-sql-mysql-single-inserts.sql")
}
