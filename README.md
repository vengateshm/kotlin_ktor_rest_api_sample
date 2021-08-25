# Ktor REST API
This is a sample REST API built using Ktor framework with H2 in-memory database and deployed in heroku.

**Heroku** - Heroku is a container-based cloud Platform as a Service (PaaS). It is used to deploy, manage, and scale modern apps.

**H2 Database** - Java SQL database - https://github.com/h2database/h2database

**Exposed** - ORM Mapper - https://github.com/JetBrains/Exposed

The following are the Ktor features used in this app
1. Authentication - Basic Auth using email and password
1. DefaultHeaders
1. CallLogging
1. Content Negotiation - GSON Library
1. StatusPages

Heroku Deployment - [Deploying Gradle apps on Heroku](https://devcenter.heroku.com/articles/deploying-gradle-apps-on-heroku)

**API Endpoints**

https://interesting-facts-app.herokuapp.com/api/users - POST (email, displayName, password in request body content - JSON)
https://interesting-facts-app.herokuapp.com/api/facts - POST (description in request body content - JSON and Basic Auth)
https://interesting-facts-app.herokuapp.com/api/facts/all - GET (Basic Auth)

**References**

https://ktor.io/learn/

https://github.com/PacktPublishing/Hands-On-Kotlin-Web-Development-with-Ktor-

https://www.raywenderlich.com/7265034-ktor-rest-api-for-mobile#toc-anchor-002

https://medium.com/better-programming/ktor-in-server-side-development-databases-b91a3bbe674f
