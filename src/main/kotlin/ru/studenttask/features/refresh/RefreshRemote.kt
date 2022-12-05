package ru.studenttask.features.refresh

import kotlinx.serialization.Serializable

@Serializable
data class RefreshReceiveRemote(
    val email: String
)

@Serializable
data class RefreshResponseRemote(
    val accessToken: String,
    val refreshToken: String
)
