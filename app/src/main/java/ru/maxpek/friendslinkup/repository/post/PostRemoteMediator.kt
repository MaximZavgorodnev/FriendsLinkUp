package ru.maxpek.friendslinkup.repository.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dao.PostRemoteKeyDao
import ru.maxpek.friendslinkup.db.PostAppDb
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.entity.PostRemoteKeyEntity
import ru.maxpek.friendslinkup.entity.toEntity
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val postDao: PostDao,
    private val postRemoteKeyDao: PostRemoteKeyDao,
    private val db: PostAppDb,

): RemoteMediator<Int, PostEntity>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, PostEntity>): MediatorResult {
        try {
            val response = when (loadType) {

                LoadType.REFRESH -> apiService.getLatest(state.config.pageSize)
                LoadType.PREPEND -> {
                    val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getAfter(id, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getBefore(id, state.config.pageSize)
                }
//                LoadType.REFRESH -> {
//                    if (postDao.isEmpty()){
//                        apiService.getLatest(state.config.pageSize)
//                    } else {
//                        val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(
//                            endOfPaginationReached = false
//                        )
//                        apiService.getAfter(id, state.config.pageSize)
//                    }
//                }
//                LoadType.PREPEND -> null
//                LoadType.APPEND -> {
//                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(
//                        endOfPaginationReached = false
//                    )
//                    apiService.getBefore(id, state.config.pageSize)
//                }
            }
//            if (response != null) {
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val body = response.body() ?: throw ApiError(
                response.code(),
                response.message(),
            )

            db.withTransaction {
                when (loadType) {
//                    LoadType.REFRESH -> {
//                        if (postRemoteKeyDao.isEmpty()) {
//                            postRemoteKeyDao.insert(
//                                listOf(
//                                    PostRemoteKeyEntity(
//                                        type = PostRemoteKeyEntity.KeyType.AFTER,
//                                        id = body.first().id,
//                                    ),
//                                    PostRemoteKeyEntity(
//                                        type = PostRemoteKeyEntity.KeyType.BEFORE,
//                                        id = body.last().id,
//                                    ),
//                                )
//                            )
//                        } else {
//                            postRemoteKeyDao.insert(
//                                PostRemoteKeyEntity(
//                                    type = PostRemoteKeyEntity.KeyType.AFTER,
//                                    id = body.first().id,
//                                )
//                            )
//                        }
//                    }
//                    LoadType.PREPEND -> {
//                        postRemoteKeyDao.insert(
//                            PostRemoteKeyEntity(
//                                type = PostRemoteKeyEntity.KeyType.AFTER,
//                                id = body.first().id,
//                            )
//                        )
//                    }
//                    LoadType.APPEND -> {
//                        postRemoteKeyDao.insert(
//                            PostRemoteKeyEntity(
//                                type = PostRemoteKeyEntity.KeyType.BEFORE,
//                                id = body.last().id,
//                            )
//                        )
//                    }
                    LoadType.REFRESH -> {
                        postRemoteKeyDao.removeAll()
                        postRemoteKeyDao.insert(
                            listOf(
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.AFTER,
                                    id = body.first().id,
                                ),
                                PostRemoteKeyEntity(
                                    type = PostRemoteKeyEntity.KeyType.BEFORE,
                                    id = body.last().id,
                                ),
                            )
                        )
                        postDao.removeAll()
                    }

                    LoadType.PREPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.AFTER,
                                id = body.first().id,
                            )
                        )
                    }
                    LoadType.APPEND -> {
                        postRemoteKeyDao.insert(
                            PostRemoteKeyEntity(
                                type = PostRemoteKeyEntity.KeyType.BEFORE,
                                id = body.last().id,
                            )
                        )
                    }
                }
                postDao.insert(body.toEntity())
            }

            return MediatorResult.Success(endOfPaginationReached = body.isEmpty())
        }
        catch (e: Exception){
            println(e)
            return MediatorResult.Error(e)
        }

    }


}