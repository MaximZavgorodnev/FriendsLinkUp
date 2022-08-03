package ru.maxpek.friendslinkup.repository.listOfUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject


class ListOfUserRepositoryImpl @Inject constructor(
    private val apiService: ApiService): ListOfUserRepository{
    override suspend fun loadUsers(): LiveData<List<UserRequested>> =  liveData {
        try {
            val response = apiService.getUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            response.body() ?: throw ApiError(response.code(), response.message())

        } catch (e: IOException) {
            throw NetworkError
        }
    }

}