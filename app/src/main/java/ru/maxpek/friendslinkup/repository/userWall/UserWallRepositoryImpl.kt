package ru.maxpek.friendslinkup.repository.userWall

import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dto.JobResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val listJob = listOf<JobResponse>()
val user = UserRequested()
class UserWallRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
): UserWallRepository {
    override val data: MutableLiveData<List<JobResponse>> = MutableLiveData(listJob)
    override val userData: MutableLiveData<UserRequested> = MutableLiveData(user)

    override suspend fun getJobUser(id: String) {
        val usersList: List<JobResponse>
        try {
            val response = apiService.getUserJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            data.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getUser(id: Int) {
        val user: UserRequested
        try {
            val response = apiService.getUser(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            user = response.body() ?: throw ApiError(response.code(), response.message())
            userData.postValue(user)
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}