package ru.studenttask.features.refresh

import kotlinx.serialization.Serializable

@Serializable
data class RefreshReceiveRemote(
    val email: String,
    val refreshToken: String
)

@Serializable
data class RefreshResponseRemote(
    val accessToken: String,
    val refreshToken: String
)
