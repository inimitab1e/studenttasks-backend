package ru.studenttask.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.database.tokens.TokenDTO
import ru.studenttask.database.tokens.Tokens
import ru.studenttask.database.users.Users
import ru.studenttask.secure.Hash.SHA256HashingService
import ru.studenttask.secure.Hash.SaltedHash
import ru.studenttask.secure.JWT.JwtConfig
import java.util.*


fun Application.configureLoginRouting() {

    val hashingService = SHA256HashingService()
    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    routing {
        post("/login") {
            val receive = call.receive<LoginReceiveRemote>()
            val userDTO = Users.fetchUser(receive.email)

            if (userDTO == null) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else {

                val isValidPassword = hashingService.verify(
                    value = receive.password,
                    saltedHash = SaltedHash(
                        hash = userDTO.password,
                        salt = userDTO.salt
                    )
                )

                if (isValidPassword) {
                    val accessToken = jwtConfig.generateAccessToken(JwtConfig.JwtUser(receive.email))
                    val refreshToken = jwtConfig.generateRefreshToken(JwtConfig.JwtUser(receive.email))
                    Tokens.insert(
                        TokenDTO(
                            rowId = UUID.randomUUID().toString(),
                            email = receive.email,
                            token = accessToken
                        )
                    )
                    call.respond(LoginResponseRemote(accessToken = accessToken, refreshToken = refreshToken))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid password")
                }
            }
        }
    }
}