package ru.studenttask.secure.Hash

data class SaltedHash(
    val hash: String,
    val salt: String
)