package ru.maxpek.friendslinkup.repository.myWall.job

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.JobDao
import ru.maxpek.friendslinkup.dto.Job
import javax.inject.Inject

class MyWallJobRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: JobDao

): MyWallJobRepository {
    override val data: Flow<PagingData<Job>>
        get() = TODO("Not yet implemented")

    override suspend fun removeById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun save(job: Job) {
        TODO("Not yet implemented")
    }
}