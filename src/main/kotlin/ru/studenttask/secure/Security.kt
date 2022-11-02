package ru.studenttask.secure

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*


fun Application.configureSecurity() {
    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

}