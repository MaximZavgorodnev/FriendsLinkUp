package ru.maxpek.friendslinkup.repository.userWall


import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.dto.JobResponse

interface UserWallRepository {
    val data: MutableLiveData<List<JobResponse>>
    suspend fun getJobUser(id: String)
}