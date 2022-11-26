package ru.studenttask.features.refresh

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.database.tokens.Tokens
import ru.studenttask.secure.JWT.JwtConfig

fun Application.configureRefreshRouting() {

    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    routing {
        post("/refresh-tokens") {
            val receive = call.receive<RefreshReceiveRemote>()
            val refreshDTO = Tokens.fetchTokens(receive.refreshToken)

            if (refreshDTO == null) {
                call.respond(HttpStatusCode.BadRequest, "Unauthorized user!")
            } else {
                val newAccessToken = jwtConfig.generateAccessToken(receive.email)
                val newRefreshToken = jwtConfig.generateRefreshToken(receive.email)

                Tokens.updateToken(receive.email, receive.refreshToken)

                call.respond(RefreshResponseRemote(accessToken = newAccessToken, refreshToken = newRefreshToken))
            }
        }
    }
}