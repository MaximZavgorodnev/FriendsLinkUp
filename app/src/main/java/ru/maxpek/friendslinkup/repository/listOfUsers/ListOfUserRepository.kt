package ru.maxpek.friendslinkup.repository.listOfUsers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.UserRequested

interface ListOfUserRepository {
    val dataUsers: MutableLiveData<List<UserRequested>>
    suspend fun loadUsers()
}