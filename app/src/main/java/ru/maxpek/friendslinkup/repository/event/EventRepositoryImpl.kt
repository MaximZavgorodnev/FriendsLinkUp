package ru.maxpek.friendslinkup.repository.event

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.EventDao
import ru.maxpek.friendslinkup.dto.EventResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.entity.EventEntity
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val emptyList = listOf<UserRequested>()

@OptIn(ExperimentalPagingApi::class)
class EventRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mediator: EventRemoteMediator,
    private val dao: EventDao
) : EventRepository {
    override val data: Flow<PagingData<EventResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = { dao.getAll() },
            remoteMediator = mediator
        ).flow.map {
            it.map(EventEntity::toDto)
        }
    override val dataUsersSpeakers: MutableLiveData<List<UserRequested>> =
        MutableLiveData(emptyList)


    override suspend fun loadUsersSpeakers(list: List<Int>) {
        val usersList = mutableListOf<UserRequested>()
        try {
            list.forEach {
                val response = apiService.getUser(it)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                usersList.add(
                    response.body() ?: throw ApiError(
                        response.code(),
                        response.message()
                    )
                )
            }
            dataUsersSpeakers.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun removeById(id: Int) {
        try {
            val response = apiService.removeByIdEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            dao.removeById(id)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun likeById(id: Int): EventResponse {
        try {
            val response = apiService.likeByIdEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun disLikeById(id: Int): EventResponse {
        try {
            val response = apiService.dislikeByIdEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun participateInEvent(id: Int): EventResponse {
        try {
            val response = apiService.participateInEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun doNotParticipateInEvent(id: Int): EventResponse {
        try {
            val response = apiService.doNotParticipateInEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(response.code(), response.message())
            dao.insert(EventEntity.fromDto(body))
            return body
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getEvent(id: Int): EventResponse {
        try {
            val response = apiService.getEvent(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            return response.body() ?: throw ApiError(response.code(), response.message())
        } catch (e: IOException) {
            throw NetworkError
        }
    }
}