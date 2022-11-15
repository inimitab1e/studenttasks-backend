package ru.studenttask.secure.JWT

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import mu.KotlinLogging
import ru.studenttask.plugins.AuthorizationException
import ru.studenttask.plugins.RoleBasedModel
import ru.studenttask.plugins.UserSession


fun Application.configureSecurity() {
    val jwtConfig = JwtConfig(System.getenv("JWT-SECRET"))

    val logger = KotlinLogging.logger {}

    install(Sessions) {
        cookie<UserSession>("ktor_session_cookie", SessionStorageMemory())
        cookie<OriginalRequestURI>("original_request_cookie")
    }

    install(Authentication) {
        jwt {
            jwtConfig.configureKtorFeature(this)
        }

        session<UserSession> {
            challenge {
                logger.info { "No valid session found for this route, redirecting to login form" }
                call.sessions.set(OriginalRequestURI(call.request.uri))
                call.respondRedirect("/login")
            }
            validate { session: UserSession ->
                logger.info { "User ${session.name} logged in by existing session" }
                session
            }
        }
    }

    install(RoleBasedModel) {
        getRoles { (it as UserSession).roles }
    }

}