package ru.maxpek.friendslinkup.repository.user

import ru.maxpek.friendslinkup.dto.User

interface UserRepository {
    suspend fun onSignIn(user: User)
    suspend fun onSignUp(login: String, pass: String, name: String)
}