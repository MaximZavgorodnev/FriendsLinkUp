package ru.maxpek.friendslinkup.repository.userWall


import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.dto.Job
import ru.maxpek.friendslinkup.dto.UserRequested

interface UserWallRepository {
    val data: MutableLiveData<List<Job>>
    val userData: MutableLiveData<UserRequested>
    suspend fun getJobUser(id: String)
    suspend fun getUser(id: Int)
}