package ru.maxpek.friendslinkup.repository.user

import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dto.UserRegistration
import ru.maxpek.friendslinkup.dto.UserResponse
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import ru.maxpek.friendslinkup.error.UnknownError

import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private  val appAuth: AppAuth
): UserRepository {

    override suspend fun onSignIn(userResponse: UserResponse) {
        try {
            val response = apiService.onSignIn(userResponse.login, userResponse.password)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())
            val token = authState.token ?: throw UnknownError
            val idUser = authState.id.toInt()
            val responseUser = apiService.getUser(idUser)
            if (!responseUser.isSuccessful) {
                throw ApiError(responseUser.code(), responseUser.message())
            }
            val avatarUser = responseUser.body()?.avatar
            val name = responseUser.body()?.name
            appAuth.setAuth(authState.id, token, avatarUser,name)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun onSignUp(user: UserRegistration) {
        try {

            val response = if (user.file != null) {
                apiService.onSignUpHasAva(user.login, user.password, user.name, user.file)
            } else {
                    apiService.onSignUpNoAva(user.login, user.password, user.name, user.file)}
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val authState = response.body() ?: throw ApiError(response.code(), response.message())

            val token = authState.token ?: throw UnknownError
            val idUser = authState.id.toInt()
            val responseUser = apiService.getUser(idUser)
            if (!responseUser.isSuccessful) {
                throw ApiError(responseUser.code(), responseUser.message())
            }
            val avatarUser = responseUser.body()?.avatar
            val name = responseUser.body()?.name
            appAuth.setAuth(authState.id, token, avatarUser,name)
        } catch (e: IOException) {
            throw NetworkError
        } catch (e: Exception) {
            throw UnknownError
        }
    }

}