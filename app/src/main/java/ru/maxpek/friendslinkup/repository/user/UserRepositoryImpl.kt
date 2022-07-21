package ru.maxpek.friendslinkup.repository.user

import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dto.User
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import ru.maxpek.friendslinkup.error.UnknownError

import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private  val appAuth: AppAuth
): UserRepository {

    override suspend fun onSignIn(user: User) {
        try {
            val response = apiService.onSignIn(user.login, user.password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            val token = authState.token ?: throw UnknownError
            appAuth.setAuth(authState.id, token)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

    override suspend fun onSignUp(login: String, pass: String, name: String) {
        try {
            val response = apiService.onSignUp(login, pass, name)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            val token = authState.token ?: throw UnknownError
            appAuth.setAuth(authState.id, token)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}