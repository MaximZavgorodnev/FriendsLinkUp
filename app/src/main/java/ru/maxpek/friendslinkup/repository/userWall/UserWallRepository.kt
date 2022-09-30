package ru.maxpek.friendslinkup.repository.userWall

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.JobResponse

interface UserWallRepository {
    val data: Flow<PagingData<JobResponse>>
    suspend fun removeAll()
}