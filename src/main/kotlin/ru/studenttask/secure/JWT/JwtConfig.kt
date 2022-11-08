package ru.studenttask.secure.JWT

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtConfig(jwtSecret: String) {

    companion object Constants {
        // jwt config
        private const val jwtIssuer = "ru.studenttask"
        private const val jwtRealm = "ru.studenttask"
        private const val expiresIn = 120000

        // claims
        private const val CLAIM_LOGIN = "login"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateToken(user: JwtUser): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresIn))
        .withClaim(CLAIM_LOGIN, user.login)
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Config) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate {
            val login = it.payload.getClaim(CLAIM_LOGIN).asString()

            if (login != null) {
                JwtUser(login)
            } else {
                null
            }
        }
    }

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(val login: String): Principal

}