package ru.maxpek.friendslinkup.repository.post

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.PostResponse

class PostRepositoryImpl : PostRepository {
    override val data: Flow<PagingData<PostResponse>>
        get() = TODO("Not yet implemented")

    override suspend fun getAll() {
        TODO("Not yet implemented")
    }

    override fun getNewerCount(id: Long): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun save(postResponse: PostResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun likeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun disLikeById(id: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }

    override suspend fun isSize(): Long {
        TODO("Not yet implemented")
    }
}