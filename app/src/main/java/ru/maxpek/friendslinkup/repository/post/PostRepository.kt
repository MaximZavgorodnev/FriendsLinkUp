package ru.maxpek.friendslinkup.repository.post

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.PostResponse

interface PostRepository {
    val data: Flow<PagingData<PostResponse>>
    suspend fun getAll()
    fun getNewerCount(id: Long): Flow<Int>
    suspend fun save(postResponse: PostResponse)
    suspend fun removeById(id: Long)
    suspend fun likeById(id: Long)
    suspend fun disLikeById(id: Long)
    suspend fun update()
    suspend fun isSize(): Long
}