package ru.maxpek.friendslinkup.repository.myWall.post

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested

interface MyWallPostRepository {
    val data: Flow<PagingData<PostResponse>>
    val dataUsersMentions: MutableLiveData<List<UserRequested>>
    suspend fun loadUsersMentions(list: List<Int>)
    fun getNewerCount(id: Int): Flow<Int>
    suspend fun removeById(id: Int)
    suspend fun likeById(id: Int)
    suspend fun disLikeById(id: Int)
    suspend fun removeAll()
    suspend fun getUser(id:Int)
}