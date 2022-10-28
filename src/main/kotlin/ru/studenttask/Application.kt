package ru.studenttask

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import ru.studenttask.plugins.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureSockets()
        configureSecurity()
        configureRouting()
    }.start(wait = true)
}
