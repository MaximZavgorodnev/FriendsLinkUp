package ru.maxpek.friendslinkup.repository.newPost

import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val emptyList = listOf<UserRequested>()
class NewPostRepositoryImpl @Inject constructor(
    private val apiService: ApiService): NewPostRepository {



    override val dataUsers: MutableLiveData<List<UserRequested>> = MutableLiveData(emptyList)


    override suspend fun loadUsers(){
        val usersList: List<UserRequested>
        try {
            val response = apiService.getUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            dataUsers.postValue(usersList)

        } catch (e: IOException) {
            throw NetworkError
        }
    }

}