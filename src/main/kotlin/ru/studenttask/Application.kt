package ru.studenttask

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import ru.studenttask.features.login.configureLoginRouting
import ru.studenttask.features.register.configureRegisterRouting
import ru.studenttask.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureSecurity()
        configureRouting()
        configureRegisterRouting()
        configureLoginRouting()
        configureSerialization()
    }.start(wait = true)
}
