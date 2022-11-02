package ru.studenttask.database.users

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    private val id = Users.varchar("id", 50)
    private val username = Users.varchar("username", 50)
    private val password = Users.varchar("password", 256)
    private val email = Users.varchar("email", 50)
    private val salt = Users.varchar("salt", 256)

    fun insert(userDTO: UserDTO) {
        transaction {
            Users.insert {
                it[id] = userDTO.id
                it[password] = userDTO.password
                it[username] = userDTO.username
                it[email] = userDTO.email
                it[salt] = ""
            }
        }
    }

    fun fetchUser(email: String): UserDTO? {
        return try {
            transaction {
                val userModel = Users.select { Users.email.eq(email) }.single()
                UserDTO(
                    id = userModel[Users.id],
                    password = userModel[password],
                    username = userModel[username],
                    email = userModel[Users.email],
                    salt = ""
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}