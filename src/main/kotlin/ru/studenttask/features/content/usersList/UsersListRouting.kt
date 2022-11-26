package ru.studenttask.features.content.usersList

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.database.users.Users

fun Application.configureUsersListRouting() {

    routing {
        get("/users") {
            val usersListDTO = Users.selectAllUsers()
            val usersList: MutableList<String> = mutableListOf()
            for (item in usersListDTO) {
                usersList.add(item.username)
                usersList.add(item.email)
            }
            call.respond(UsersResponseRemote(usersResponseList = usersList))
        }
    }

    routing {
        authenticate {
            get("secret") {
                val principal = call.principal<JWTPrincipal>()
                val email = principal?.payload?.getClaim("email")?.asString()
                val expiresAt = principal?.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $email! Token is expired at $expiresAt ms.")
            }
        }
    }
}