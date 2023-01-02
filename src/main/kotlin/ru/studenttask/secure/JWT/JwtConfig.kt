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
        private const val jwtAud = "inimitab1e"
        //604800000
        private const val expiresInAccess = 604800000
        private const val expiresInRefresh = 2419200000

        // claims
        private const val CLAIM_EMAIL = "email"
    }

    private val jwtAlgorithm = Algorithm.HMAC512(jwtSecret)
    private val jwtVerifier: JWTVerifier = JWT
        .require(jwtAlgorithm)
        .withAudience(jwtAud)
        .withIssuer(jwtIssuer)
        .build()

    /**
     * Generate a token for a authenticated user
     */
    fun generateAccessToken(email: String): String = JWT.create()
        .withAudience(jwtAud)
        .withIssuer(jwtIssuer)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresInAccess))
        .withClaim(CLAIM_EMAIL, email)
        .sign(jwtAlgorithm)

    fun generateRefreshToken(email: String) = JWT.create()
        .withAudience(jwtAud)
        .withIssuer(jwtIssuer)
        .withClaim(CLAIM_EMAIL, email)
        .withExpiresAt(Date(System.currentTimeMillis() + expiresInRefresh))
        .sign(jwtAlgorithm)

    /**
     * Configure the jwt ktor authentication feature
     */
    fun configureKtorFeature(config: JWTAuthenticationProvider.Config) = with(config) {
        verifier(jwtVerifier)
        realm = jwtRealm
        validate { jwtCredential: JWTCredential ->
            kotlin.run {
                val email = jwtCredential.payload.getClaim(CLAIM_EMAIL).asString()
                if (email.isNotEmpty()) {
                    JWTPrincipal(jwtCredential.payload)
                } else {
                    null
                }
            }
        }
        challenge { _, _ ->
            call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
        }
    }
}