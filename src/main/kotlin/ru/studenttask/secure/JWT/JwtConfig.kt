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
        .withClaim("tokenType", "accessToken")
        .sign(jwtAlgorithm)

    fun generateRefreshToken(user: JwtUser) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_EMAIL, user.email)
        .withClaim("tokenType", "refreshToken")
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
    }

 /*   fun checkExpired(token: String) {
        token.
    }*/

    /**
     * POKO, that contains information of an authenticated user that is authenticated via jwt
     */
    data class JwtUser(val email: String): Principal

}