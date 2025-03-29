package com.f1db.tests.sql

import java.io.File

/**
 * Validates the generated MySQL sql dump file.
 *
 * @author Marcel Overdijk
 */
class MysqlDumpTest : AbstractMysqlDumpTest() {

    override val importFile = File("build/data/sql/f1db-sql-mysql.sql")
}
