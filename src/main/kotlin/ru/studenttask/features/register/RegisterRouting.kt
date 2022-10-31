package ru.studenttask.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.exceptions.ExposedSQLException
import ru.studenttask.database.tokens.TokenDTO
import ru.studenttask.database.tokens.Tokens
import ru.studenttask.database.users.UserDTO
import ru.studenttask.database.users.Users
import ru.studenttask.secure.JwtConfig
import ru.studenttask.utils.isValidEmail
import java.util.*

fun Application.configureRegisterRouting() {

    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }
    }

    routing {
        post("/register") {
            val registerReceiveRemote = call.receive<RegisterReceiveRemote>()
            if (!registerReceiveRemote.email.isValidEmail()) {
                call.respond(HttpStatusCode.BadRequest, "Email is not valid")
            }

            val userDTO = Users.fetchUser(registerReceiveRemote.login)
            if (userDTO != null) {
                call.respond(HttpStatusCode.Conflict, "User already exists")
            } else {
                val token = jwtConfig.generateToken(JwtConfig.JwtUser(registerReceiveRemote.login))

                try {
                    Users.insert(
                        UserDTO(
                            id = UUID.randomUUID().toString(),
                            login = registerReceiveRemote.login,
                            password = registerReceiveRemote.password,
                            email = registerReceiveRemote.email,
                            username = ""
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
                        login = registerReceiveRemote.login,
                        token = token
                    )
                )

                call.respond(RegisterResponseRemote(token = token))
            }
        }
    }
}