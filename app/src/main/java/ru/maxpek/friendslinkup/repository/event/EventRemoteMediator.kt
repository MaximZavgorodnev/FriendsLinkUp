package ru.maxpek.friendslinkup.repository.event

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.EventDao
import ru.maxpek.friendslinkup.dao.EventRemoteKeyDao
import ru.maxpek.friendslinkup.db.PostAppDb
import ru.maxpek.friendslinkup.entity.*
import ru.maxpek.friendslinkup.error.ApiError
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class EventRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val eventRemoteKeyDao: EventRemoteKeyDao,
    private val db: PostAppDb,

    ): RemoteMediator<Int, EventEntity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, EventEntity>): MediatorResult {
        try {
            val response = when (loadType) {

                LoadType.REFRESH -> apiService.getLatestEvent(state.config.pageSize)
                LoadType.PREPEND -> {
                    val id = eventRemoteKeyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getAfterEvent(id, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = eventRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getBeforeEvent(id, state.config.pageSize)
                }
            }
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(
                response.code(),
                response.message(),
            )

            db.withTransaction {
                when (loadType) {
                    LoadType.REFRESH -> {
                        eventRemoteKeyDao.removeAll()
                        eventRemoteKeyDao.insert(
                            listOf(
                                EventRemoteKeyEntity(
                                    type = EventRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id,
                                ),
                                EventRemoteKeyEntity(
                                    type = EventRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id,
                                ),
                            )
                        )
                        eventDao.removeAll()
                    }

                    LoadType.PREPEND -> {
                        eventRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                type = EventRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id,
                            )
                        )
                    }
                    LoadType.APPEND -> {
                        eventRemoteKeyDao.insert(
                            EventRemoteKeyEntity(
                                type = EventRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id,
                            )
                        )
                    }
                }
                eventDao.insert(body.toEntity())
            }

            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        }
        catch (e: Exception){
            return MediatorResult.Error(e)
        }

    }


}