package ru.maxpek.friendslinkup.repository.myWall.job

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.Job

interface MyWallJobRepository {
    val dateJob: MutableList<Job>
    suspend fun getMyJob()
    suspend fun removeById(id: Int)
    suspend fun save(job: Job)
}