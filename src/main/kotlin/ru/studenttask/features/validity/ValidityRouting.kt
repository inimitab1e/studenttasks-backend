package ru.studenttask.features.validity

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureValidityRouting() {
    routing {
        authenticate {
            get("validity") {
                call.respond(HttpStatusCode.OK, "Access token is valid")
            }
        }
    }
}