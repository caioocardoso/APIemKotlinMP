package com.stackmobile

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.expensesRouting

fun Application.configureRouting() {
    routing {
        expensesRouting()
    }
}
