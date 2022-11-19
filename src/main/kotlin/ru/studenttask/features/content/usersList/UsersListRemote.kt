package ru.studenttask.features.content.usersList


@kotlinx.serialization.Serializable
data class UsersResponseRemote(
    val usersResponseList: List<String>
)
