package ru.maxpek.friendslinkup.repository.post

import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.entity.toEntity
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject


val emptyList = listOf<UserRequested>()
@OptIn(ExperimentalPagingApi::class)
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val mediator: PostRemoteMediator,
    private val appAuth: AppAuth,
    private val dao: PostDao
) : PostRepository {
    private val memoryPosts = mutableListOf<PostResponse>()

    override val data: Flow<PagingData<PostResponse>> =
        Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {dao.getAll()},
            remoteMediator = mediator
        ).flow.map{
            it.map(PostEntity::toDto)
        }


    override val dataUsersMentions: MutableLiveData<List<UserRequested>> = MutableLiveData(emptyList)


    override suspend fun loadUsersMentions(list: List<Int>) {
        val usersList = mutableListOf<UserRequested>()
        try {
            list.forEach {
                val response = apiService.getUser(it)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                usersList.add(response.body()?: throw ApiError(response.code(), response.message()))
            }
            dataUsersMentions.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getAll() {
//        try {
//            if (memoryPosts.isNotEmpty()) {
//                coroutineScope {
//                    memoryPosts.map { post ->
//                        async {
//                            val response = apiService.save(post)
//                            if (!response.isSuccessful) {
//                                memoryPosts.add(post)
//                            }
//                            memoryPosts.remove(post)
//                            val body =
//                                response.body() ?: throw ApiError(
//                                    response.code(),
//                                    response.message()
//                                )
//                            dao.insert(PostEntity.fromDto(body))
//                        }
//                    }.awaitAll()
//                }
//            }
//            val response = apiService.getAll()
//            if (!response.isSuccessful) {
//                throw ApiError(response.code(), response.message())
//            }
//            val body = response.body() ?: throw ApiError(response.code(), response.message())
//            dao.insert(body.toEntity())
//
//        } catch (e: IOException) {
//            throw NetworkError
//        } catch (e: Exception) {
//            throw UnknownError
//        }
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