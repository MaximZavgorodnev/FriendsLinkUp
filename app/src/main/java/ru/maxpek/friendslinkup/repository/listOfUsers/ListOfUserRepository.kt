package ru.maxpek.friendslinkup.repository.listOfUsers

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.UserRequested

interface ListOfUserRepository {
    suspend fun loadUsers()
}