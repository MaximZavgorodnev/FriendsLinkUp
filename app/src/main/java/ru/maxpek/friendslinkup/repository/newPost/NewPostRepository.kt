package ru.maxpek.friendslinkup.repository.newPost

import androidx.lifecycle.MutableLiveData
import ru.maxpek.friendslinkup.dto.UserRequested

interface NewPostRepository {
    val dataUsers: MutableLiveData<List<UserRequested>>
    suspend fun loadUsers()
}