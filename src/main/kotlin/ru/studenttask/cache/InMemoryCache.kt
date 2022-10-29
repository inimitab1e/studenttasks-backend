package ru.studenttask.cache

import ru.studenttask.features.register.RegisterReceiveRemote

data class TokenCache(
    val login: String,
    val token: String
)

object InMemoryCache {
    val userList: MutableList<RegisterReceiveRemote> = mutableListOf()
    val tokenList: MutableList<TokenCache> = mutableListOf()
}