package com.onlyf1.db.plugin.extensions

import com.fasterxml.jackson.core.TreeNode
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.NullNode
import com.fasterxml.jackson.databind.node.NumericNode
import com.fasterxml.jackson.databind.node.TextNode

fun TreeNode.contains(fieldName: String): Boolean {
    return this.fieldNames().asSequence().toList().contains(fieldName)
}

fun TreeNode.asText(fieldName: String): String? {
    val fieldNode = this.get(fieldName)
    return when (fieldNode) {
        null -> null
        is NullNode -> null
        is BooleanNode -> fieldNode.asText()
        is NumericNode -> fieldNode.asText()
        is TextNode -> fieldNode.asText()
        else -> throw IllegalArgumentException("Cannot get text for field: $fieldName")
    }
}
