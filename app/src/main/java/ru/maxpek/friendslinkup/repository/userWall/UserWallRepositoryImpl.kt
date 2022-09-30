package ru.maxpek.friendslinkup.repository.userWall

import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.userWall.UserWallJobDao
import ru.maxpek.friendslinkup.dto.JobResponse
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val listJob = listOf<JobResponse>()
class UserWallRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: UserWallJobDao
): UserWallRepository {
    override val data: MutableLiveData<List<JobResponse>> = MutableLiveData(listJob)
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
}