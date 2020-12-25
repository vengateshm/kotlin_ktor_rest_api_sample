package com.facts.dao

import com.facts.model.Fact
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class FactDaoImpl(private val database: Database) : FactDao {
    override fun init() =
        transaction(database) {
            SchemaUtils.create(Facts) // Create Fact table
        }

    override fun createFact(description: String) =
        transaction(database) {
            Facts.insert {
                it[Facts.description] = description
            }
            Unit
        }

    override fun getAllFacts(): List<Fact> =
        transaction(database) {
            Facts.selectAll().map {
                Fact(it[Facts.id], it[Facts.description])
            }
        }

    override fun close() {}
}