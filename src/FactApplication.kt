package com.facts

import com.facts.dao.FactDaoImpl
import com.facts.util.H2_DB_DRIVER
import com.facts.util.H2_DB_URL
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 1441
    val server = embeddedServer(Netty, port = port) {
        factModule()
    }
    server.start(wait = true)
}

fun Application.factModule() {
    install(DefaultHeaders)
    install(CallLogging)
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
    // Init database
    val factDao = FactDaoImpl(
        Database.connect(
            url = H2_DB_URL,
            driver = H2_DB_DRIVER
        )
    )
    factDao.init()
    // Routing
    install(Routing) {
        rootRoute()
        createFact(factDao)
    }
}