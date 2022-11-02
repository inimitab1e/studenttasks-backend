package ru.studenttask.secure.JWT

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import ru.studenttask.secure.JWT.JwtConfig


fun Application.configureSecurity() {
    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

}