package ru.maxpek.friendslinkup.repository.myWall.post

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dao.myWall.MyWallPostDao
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.repository.post.PostRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MyWallPostRepositoryImpl@Inject constructor(
    private val apiService: ApiService,
    private val mediator: MyWallPostRemoteMediator,
    private val dao: PostDao
): PostRepository {
    override val data: Flow<PagingData<PostResponse>> =
    Pager(
    config = PagingConfig(pageSize = 10, enablePlaceholders = false),
    pagingSourceFactory = {dao.getAll()},
    remoteMediator = mediator
    ).flow.map{
        it.map(PostEntity::toDto)
    }
    override val dataUsersMentions: MutableLiveData<List<UserRequested>>
        get() = TODO("Not yet implemented")

    override suspend fun loadUsersMentions(list: List<Int>) {
        TODO("Not yet implemented")
    }

    override suspend fun getAll() {
        TODO("Not yet implemented")
    }

    override fun getNewerCount(id: Int): Flow<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun save(postResponse: PostResponse) {
        TODO("Not yet implemented")
    }

    override suspend fun removeById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun likeById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun disLikeById(id: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun update() {
        TODO("Not yet implemented")
    }

    override suspend fun isSize(): Int {
        TODO("Not yet implemented")
    }
}