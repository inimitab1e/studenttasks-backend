package ru.studenttask.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.database.tokens.TokenDTO
import ru.studenttask.database.tokens.Tokens
import ru.studenttask.database.users.Users
import ru.studenttask.secure.JwtConfig
import java.util.*


fun Application.configureLoginRouting() {

    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    routing {
        post("/login") {
            val receive = call.receive<LoginReceiveRemote>()
            val userDTO = Users.fetchUser(receive.email)

            if (userDTO == null) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else {
                if (userDTO.password == receive.password) {
                    val token = jwtConfig.generateToken(JwtConfig.JwtUser(receive.email))
                    Tokens.insert(
                        TokenDTO(
                            rowId = UUID.randomUUID().toString(),
                            email = receive.email,
                            token = token
                        )
                    )

                    call.respond(LoginResponseRemote(token = token))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid password")
                }
            }
        }
    }
}