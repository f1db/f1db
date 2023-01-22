package com.onlyf1.db.plugin.extensions

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate

fun NamedParameterJdbcTemplate.execute(sql: String) {
    this.jdbcOperations.execute(sql)
}

fun NamedParameterJdbcTemplate.batchUpdate(sql: String, batchValues: List<Map<String, Any?>>): IntArray {
    return this.batchUpdate(sql, batchValues.toTypedArray())
}
