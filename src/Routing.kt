package com.facts

import com.facts.dao.FactDaoImpl
import com.facts.dao.UserDaoImpl
import com.facts.model.Fact
import com.facts.model.FactsResponse
import com.facts.model.Message
import com.facts.model.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.html.*
import io.ktor.http.*
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
    route("") {
        get {
            call.respondHtml {
                head {
                    title {
                        +"Facts App"
                    }
                }
            }
        }
        get("/api") {
            call.respondHtml {
                head {
                    title {
                        +"API Version 1.0"
                    }
                }
                body {
                    val runtime = Runtime.getRuntime()
                    p { +"Hello from Ktor Netty engine running in Heroku sample application" }
                    p { +"Runtime.getRuntime().availableProcessors(): ${runtime.availableProcessors()}" }
                    p { +"Runtime.getRuntime().freeMemory(): ${runtime.freeMemory()}" }
                    p { +"Runtime.getRuntime().totalMemory(): ${runtime.totalMemory()}" }
                    p { +"Runtime.getRuntime().maxMemory(): ${runtime.maxMemory()}" }
                }
            }
        }
    }
}

fun Routing.createUser(userDao: UserDaoImpl) {
    route("/api") {
        route("/users") {
            post {
                val user = call.receive<User>()
                if (user.email.isNotEmpty() && user.displayName.isNotEmpty() && user.password.isNotEmpty()) {
                    userDao.createUser(user.email, user.displayName, user.password)
                    call.respond(status = HttpStatusCode.OK, Message(true, "User created successfully."))
                } else {
                    call.respond(status = HttpStatusCode.BadRequest, Message(false, "All fields required."))
                }
            }
        }
    }
}

fun Route.createFact(factDao: FactDaoImpl) {
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
                val user = call.principal<User>()
                val facts = factDao.getAllFacts()
                if (facts.isNotEmpty()) {
                    call.respond(FactsResponse(user?.userId, facts))
                } else {
                    call.respond(emptyList<Fact>())
                }
            }
        }
    }
}