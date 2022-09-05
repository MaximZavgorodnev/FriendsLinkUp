package ru.maxpek.friendslinkup.repository.post

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val emptyList = listOf<UserRequested>()
class PostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val appAuth: AppAuth,
    private val dao: PostDao
) : PostRepository {

    override val data: Flow<PagingData<PostResponse>>
        get() = TODO("Not yet implemented")
    override val dataUsersMentions: MutableLiveData<List<UserRequested>> = MutableLiveData(emptyList)


    override suspend fun loadUsersMentions(list: List<Int>) {
        val usersList: List<UserRequested>
        try {
            list.forEach {
                val response = apiService.getUser(it)
                if (!response.isSuccessful) {
                    throw ApiError(response.code(), response.message())
                }
                usersList = response.body() ?: throw ApiError(response.code(), response.message())
            }
            val response = apiService.getUser()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            dataUsersMentions.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

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