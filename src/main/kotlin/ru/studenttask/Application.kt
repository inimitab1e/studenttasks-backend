package ru.studenttask

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.engine.*
import io.ktor.server.cio.*
import org.jetbrains.exposed.sql.Database
import ru.studenttask.features.content.usersList.configureUsersListRouting
import ru.studenttask.features.login.configureLoginRouting
import ru.studenttask.features.refresh.configureRefreshRouting
import ru.studenttask.features.register.configureRegisterRouting
import ru.studenttask.features.validity.configureValidityRouting
import ru.studenttask.plugins.*
import ru.studenttask.secure.JWT.configureSecurity

fun main() {
    val config = HikariConfig("database.properties")
    val dataSource = HikariDataSource(config)
    Database.connect(dataSource)

    embeddedServer(CIO, port = 8080, host = "localhost") {
        configureRegisterRouting()
        configureLoginRouting()
        configureRefreshRouting()
        configureSerialization()
        configureUsersListRouting()
        configureSecurity()
        configureValidityRouting()
    }.start(wait = true)
}
