package com.facts.dao

import com.facts.model.User
import java.io.Closeable

interface UserDao : Closeable {
    fun createUser(email: String, displayName: String, passwordHash: String)
    fun findUser(email: String): User?
}