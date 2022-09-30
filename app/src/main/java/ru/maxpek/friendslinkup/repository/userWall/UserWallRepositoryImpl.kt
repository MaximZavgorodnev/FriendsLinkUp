package ru.maxpek.friendslinkup.repository.userWall

import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.userWall.UserWallJobDao
import ru.maxpek.friendslinkup.dto.JobResponse
import ru.maxpek.friendslinkup.entity.JobEntity
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class UserWallRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: UserWallJobDao
): UserWallRepository {
    override val data: Flow<PagingData<JobResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {dao.getAll()},
            remoteMediator = mediator
        ).flow.map{
            it.map(JobEntity::toDto)
        }

    override suspend fun removeAll() {
        TODO("Not yet implemented")
    }
}