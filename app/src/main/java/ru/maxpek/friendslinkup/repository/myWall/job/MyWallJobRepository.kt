package ru.maxpek.friendslinkup.repository.myWall.job

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.Job

interface MyWallJobRepository {
    val data: Flow<PagingData<Job>>
    suspend fun removeById(id: Int)
    suspend fun save(job: Job)
}