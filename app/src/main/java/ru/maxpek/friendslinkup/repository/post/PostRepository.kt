package ru.maxpek.friendslinkup.repository.post

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested

interface PostRepository {
    val data: Flow<PagingData<PostResponse>>
    val dataUsersMentions: MutableLiveData<List<UserRequested>>
    suspend fun loadUsersMentions(list: List<Int>)
    suspend fun removeById(id: Int)
    suspend fun likeById(id: Int)
    suspend fun disLikeById(id: Int)
}