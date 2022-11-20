package ru.studenttask.secure.JWT

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.*

class JwtConfig(jwtSecret: String) {

    companion object Constants {
        // jwt config
        private const val jwtIssuer = "ru.studenttask"
        private const val jwtRealm = "ru.studenttask"
        private const val expiresInAccess = 120000
        private const val expiresInRefresh = 120000000

        // claims
        private const val CLAIM_EMAIL = "email"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateAccessToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresInAccess))
        .withClaim(CLAIM_EMAIL, user.email)
        .sign(jwtAlgorithm)

    fun generateRefreshToken(user: JwtUser) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_EMAIL, user.email)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresInRefresh))
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Config) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val email = it.payload.getClaim(CLAIM_EMAIL).asString()

            if (email != null) {
                JwtUser(email)
            } else {
                null
            }
        }
        challenge { defaultScheme, realm ->
            call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
        }
    }

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(val email: String): Principal

}