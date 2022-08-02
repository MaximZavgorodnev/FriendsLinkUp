package ru.maxpek.friendslinkup.repository.listOfUsers

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import ru.maxpek.friendslinkup.error.UnknownError
import java.io.IOException
import javax.inject.Inject


class ListOfUserRepositoryImpl @Inject constructor(
    private val apiService: ApiService): ListOfUserRepository{

    private var users = mutableListOf<UserRequested>()
    override suspend fun loadUsers() {
        try {
            val response = apiService.getUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            users = response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        }
    }

}