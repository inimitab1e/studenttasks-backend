package ru.studenttask.database.tokens

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.studenttask.database.users.Users

object Tokens : Table() {
    private val id = Tokens.varchar("id", 50)
    private val email = Tokens.varchar("email", 50)
    private val token = Tokens.varchar("token", 600)

    fun insert(tokenDTO: TokenDTO) {
        transaction {
            Tokens.insert {
                it[id] = tokenDTO.rowId
                it[email] = tokenDTO.email
                it[token] = tokenDTO.token
            }
        }
    }

/*    fun delete(token: String) {
        transaction {
            Tokens.deleteWhere { Tokens.token eq token }
        }
    }*/

    fun updateToken(email: String, newToken: String) {
        transaction {
            Tokens.update({ Tokens.email eq email }) {
                it[token] = newToken
            }
        }
    }

    fun fetchTokens(refresh: String): TokenDTO? {
        return try {
            transaction {
                val refreshModel = Tokens.select { Tokens.token.eq(token) }.single()
                TokenDTO(
                    rowId = refreshModel[Tokens.id],
                    email = refreshModel[Tokens.email],
                    token = refreshModel[Tokens.token]
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}