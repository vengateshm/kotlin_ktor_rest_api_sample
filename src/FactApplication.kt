package com.facts

import com.facts.dao.FactDaoImpl
import com.facts.dao.UserDaoImpl
import com.facts.util.H2_DB_DRIVER
import com.facts.util.H2_DB_URL
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

const val BASIC_AUTH_REALM = "Facts App Server Realm"
const val BASIC_AUTH = "Facts App Server"

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 1441
    val server = embeddedServer(Netty, port = port) {
        factModule()
    }
    server.start(wait = true)
}

fun Application.factModule() {
    // Init database
    val database = Database.connect(url = H2_DB_URL, driver = H2_DB_DRIVER)
    val factDao = FactDaoImpl(database)
    val userDao = UserDaoImpl(database)

    install(DefaultHeaders)
    install(CallLogging)

    install(Authentication) {
        basic(BASIC_AUTH) {
            realm = BASIC_AUTH_REALM
            validate {
                val user = userDao.findUser(it.name) ?: return@validate null
                if (it.name == user.email && it.password == user.password) {
                    return@validate user
                } else {
                    return@validate null
                }
            }
        }
    }

    // Content negotiation
    install(ContentNegotiation) {
        jackson {
            configure(SerializationFeature.INDENT_OUTPUT, true)
        }
    }
    install(StatusPages) {
        this.exception<Throwable> { e ->
            call.respondText(e.localizedMessage, contentType = ContentType.Text.Plain)
            throw e
        }
    }

    // Routing
    install(Routing) {
        rootRoute()
        createUser(userDao)
        authenticate(BASIC_AUTH) {
            createFact(factDao)
        }
    }
}