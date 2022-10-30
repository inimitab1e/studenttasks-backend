package ru.studenttask

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.studenttask.features.login.configureLoginRouting
import ru.studenttask.features.register.configureRegisterRouting
import ru.studenttask.plugins.*

fun main() {
    val config = HikariConfig("database.properties")
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureSecurity()
        configureRouting()
        configureRegisterRouting()
        configureLoginRouting()
        configureSerialization()
    }.start(wait = true)
}
