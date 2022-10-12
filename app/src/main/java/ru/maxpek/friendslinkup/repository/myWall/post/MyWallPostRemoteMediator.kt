package ru.maxpek.friendslinkup.repository.myWall.post

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.myWall.MyWallPostDao
import ru.maxpek.friendslinkup.dao.myWall.MyWallRemoteKeyDao
import ru.maxpek.friendslinkup.db.WallPostAppDb
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.entity.PostRemoteKeyEntity
import ru.maxpek.friendslinkup.entity.toEntity
import ru.maxpek.friendslinkup.error.ApiError
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MyWallPostRemoteMediator @Inject constructor(
    private val apiService: ApiService,
    private val postDao: MyWallPostDao,
    private val postRemoteKeyDao: MyWallRemoteKeyDao,
    private val db: WallPostAppDb
) : RemoteMediator<Int, PostEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        try {
            val response = when (loadType) {

                LoadType.REFRESH -> apiService.getPostMyWallLatest(state.config.pageSize)
                LoadType.PREPEND -> {
                    val id = postRemoteKeyDao.max() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getPostMyWallAfter(id, state.config.pageSize)
                }
                LoadType.APPEND -> {
                    val id = postRemoteKeyDao.min() ?: return MediatorResult.Success(
                        endOfPaginationReached = false
                    )
                    apiService.getPostMyWallBefore(id, state.config.pageSize)
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
        } catch (e: Exception) {
            println(e)
            return MediatorResult.Error(e)
        }

    }


}