package ru.maxpek.friendslinkup.repository.user

import ru.maxpek.friendslinkup.dto.UserRegistration
import ru.maxpek.friendslinkup.dto.UserResponse

interface UserRepository {
    suspend fun onSignIn(userResponse: UserResponse)
    suspend fun onSignUp(user: UserRegistration)
}