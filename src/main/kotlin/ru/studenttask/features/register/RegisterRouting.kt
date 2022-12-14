package ru.studenttask.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.studenttask.database.tokens.TokenDTO
import ru.studenttask.database.tokens.Tokens
import ru.studenttask.database.users.UserDTO
import ru.studenttask.database.users.Users
import ru.studenttask.secure.Hash.SHA256HashingService
import ru.studenttask.secure.JWT.JwtConfig
import ru.studenttask.utils.isValidEmail
import ru.studenttask.utils.isValidPassword
import java.util.*

fun Application.configureRegisterRouting() {

    val hashingService = SHA256HashingService()
    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    routing {
        post("/register") {
            val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
            if (!isValidEmail(registerReceiveRemote.email)) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }

            val isValidPass = isValidPassword(registerReceiveRemote.password)

            if(isValidPass == "Empty field") {
                call.respond(HttpStatusCode.Conflict, "Password can not be empty")
                return@post
            } else if (isValidPass == "Less then 8 symbols") {
                call.respond(HttpStatusCode.Conflict, "Password can not be less then 8 symbols")
                return@post
            }

            val userDTO = Users.fetchUser(registerReceiveRemote.email)
            if (userDTO != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } else {
                val saltedHash = hashingService.generateSaltedHash(registerReceiveRemote.password)
                val accessToken = jwtConfig.generateAccessToken(registerReceiveRemote.email)
                val refreshToken = jwtConfig.generateRefreshToken(registerReceiveRemote.email)
                try {
                    Users.insert(
                        UserDTO(
                            id = UUID.randomUUID().toString(),
                            email = registerReceiveRemote.email,
                            password = saltedHash.hash,
                            username = registerReceiveRemote.username,
                            salt = saltedHash.salt
                        )
                    )
                } catch (e: ExposedSQLException) {
                    call.respond(HttpStatusCode.Conflict, "User already exists")
                } catch (e: Exception) {
                    call.respond(HttpStatusCode.BadRequest, "Can't create user ${e.localizedMessage}")
                }

                Tokens.insert(
                    TokenDTO(
                        rowId = UUID.randomUUID().toString(),
                        email = registerReceiveRemote.email,
                        token = refreshToken
                    )
                )

                call.respond(RegisterResponseRemote(accessToken = accessToken, refreshToken = refreshToken))
            }
        }
    }
}