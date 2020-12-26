package com.facts.dao

import com.facts.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDaoImpl(private val database: Database) : UserDao {
    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    override fun createUser(email: String, displayName: String, passwordHash: String) = transaction(database) {
        Users.insert {
            it[Users.email] = email
            it[Users.displayName] = displayName
            it[Users.passwordHash] = passwordHash
        }
        Unit
    }

    override fun findUser(email: String) = transaction(database) {
        Users.select { Users.email.eq(email) }.map {
            it.toUser()
        }.singleOrNull()
    }

    override fun close() {}
}

fun ResultRow?.toUser(): User? {
    if (this == null) return null
    return User(
        userId = this[Users.userId],
        email = this[Users.email],
        displayName = this[Users.displayName],
        password = this[Users.passwordHash]
    )
}