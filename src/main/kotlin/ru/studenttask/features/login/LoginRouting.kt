package ru.studenttask.features.login

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.studenttask.cache.InMemoryCache
import ru.studenttask.cache.TokenCache
import java.util.*


fun Application.configureLoginRouting() {

    routing {
        post("/login") {
            val receive = call.receive(LoginReceiveRemote::class)
            val first = InMemoryCache.userList.firstOrNull { it.login == receive.login }

            if (first == null) {
                call.respond(HttpStatusCode.BadRequest, "User not found")
            } else {
                if (first.password == receive.password) {
                    val token = UUID.randomUUID().toString()
                    InMemoryCache.tokenList.add(TokenCache(login = receive.login, token = token))
                    call.respond(LoginResponseRemote(token = token))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid password")
                }
            }


            call.respond(HttpStatusCode.BadRequest)
        }
    }
}