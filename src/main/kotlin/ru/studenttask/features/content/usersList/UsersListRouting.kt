package ru.studenttask.features.content.usersList

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.database.users.Users

fun Application.configureUsersListRouting() {
    routing {
        authenticate {
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
    }
}