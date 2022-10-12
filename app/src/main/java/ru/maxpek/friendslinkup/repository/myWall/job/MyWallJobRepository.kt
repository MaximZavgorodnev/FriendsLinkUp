package ru.maxpek.friendslinkup.repository.myWall.job

import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.dto.Job

interface MyWallJobRepository {
    val dateJob: MutableLiveData<MutableList<Job>>
    suspend fun getMyJob()
    suspend fun removeById(id: Int)
    suspend fun save(job: Job)
}