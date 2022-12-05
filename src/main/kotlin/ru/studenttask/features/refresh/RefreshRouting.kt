package ru.studenttask.features.refresh

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.secure.JWT.JwtConfig

fun Application.configureRefreshRouting() {

    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    routing {
        authenticate {
            post("/refresh") {
                val receive = call.receive<RefreshReceiveRemote>()

                val newAccessToken = jwtConfig.generateAccessToken(receive.email)
                val newRefreshToken = jwtConfig.generateRefreshToken(receive.email)

                call.respond(RefreshResponseRemote(accessToken = newAccessToken, refreshToken = newRefreshToken))
            }
        }
    }
}
