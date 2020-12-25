package com.facts.dao

import org.jetbrains.exposed.sql.Table

object Facts : Table() {
    override val tableName: String
        get() = "fact"
    val id = integer("id").primaryKey().autoIncrement()
    val description = varchar("description", 30000)
}