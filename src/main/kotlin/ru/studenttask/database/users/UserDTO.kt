package ru.studenttask.database.users

class UserDTO(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val salt: String
)