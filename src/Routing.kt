package com.facts

import com.facts.dao.FactDaoImpl
import com.facts.model.Fact
import com.facts.model.Message
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.p
import kotlinx.html.title

fun Routing.rootRoute() {
    // For the root / route, we respond with an Html.
    // The `respondHtml` extension method is available at the `ktor-html-builder` artifact.
    // It provides a DSL for building HTML to a Writer, potentially in a chunked way.
    // More information about this DSL: https://github.com/Kotlin/kotlinx.html
    route("/api"){
        get {
            call.respondHtml {
                head {
                    title { +"Ktor: Docker" }
                }
                body {
                    val runtime = Runtime.getRuntime()
                    p { +"Hello from Ktor Netty engine running in Docker sample application" }
                    p { +"Runtime.getRuntime().availableProcessors(): ${runtime.availableProcessors()}" }
                    p { +"Runtime.getRuntime().freeMemory(): ${runtime.freeMemory()}" }
                    p { +"Runtime.getRuntime().totalMemory(): ${runtime.totalMemory()}" }
                    p { +"Runtime.getRuntime().maxMemory(): ${runtime.maxMemory()}" }
                    p { +"System.getProperty(\"user.name\"): ${System.getProperty("user.name")}" }
                }
            }
        }
    }
}

fun Routing.createFact(factDao: FactDaoImpl) {
    route("/api") {
        route("/facts") {
            post {
                val fact = call.receive<Fact>()
                if (fact.description.isNotEmpty()) {
                    factDao.createFact(description = fact.description)
                    call.respond(Message(true, "Fact created successfully."))
                } else {
                    call.respond(Message(false, "Description for a fact is mandatory."))
                }
            }
            get("/all") {
                val facts = factDao.getAllFacts()
                if (facts.isNotEmpty()) {
                    call.respond(facts)
                } else {
                    call.respond(emptyList<Fact>())
                }
            }
        }
    }
}