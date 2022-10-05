package ru.maxpek.friendslinkup.repository.myWall.job

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.JobDao
import ru.maxpek.friendslinkup.dto.Job
import ru.maxpek.friendslinkup.entity.JobEntity
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.entity.toDto
import ru.maxpek.friendslinkup.entity.toEntity
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

class MyWallJobRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: JobDao

): MyWallJobRepository {
    override val dateJob: MutableList<Job> = mutableListOf()

    override suspend fun getMyJob() {
        val usersList: List<Job>
        try {
            val response = apiService.getMyJob()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            dateJob.addAll(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun removeById(id: Int) {
        try {
            val response = apiService.deleteJob(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dateJob.removeAt(id)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun save(job: Job) {
        try {
            val response = apiService.addJob(job)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dateJob.add(body)
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}