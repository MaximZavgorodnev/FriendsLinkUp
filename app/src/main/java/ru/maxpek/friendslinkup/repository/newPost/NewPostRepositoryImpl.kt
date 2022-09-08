package ru.maxpek.friendslinkup.repository.newPost

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dto.*
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject

val emptyList = listOf<UserRequested>()


val attachment = Attachment("", AttachmentType.IMAGE)
class NewPostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: PostDao
): NewPostRepository {



    override val dataUsers: MutableLiveData<List<UserRequested>> = MutableLiveData(emptyList)
    override val dataAttachment: MutableLiveData<Attachment> = MutableLiveData(attachment)


    override suspend fun loadUsers(){
        val usersList: List<UserRequested>
        try {
            val response = apiService.getUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            dataUsers.postValue(usersList)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun addPictureToThePost(attachmentType: AttachmentType, image: MultipartBody.Part) {

        try {
            val response = apiService.addMultimedia(image)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val mediaResponse = response.body() ?: throw ApiError(response.code(), response.message())
            val attachment = Attachment(mediaResponse.url, attachmentType)
            dataAttachment.postValue(attachment)
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun addPost(post: PostCreateRequest) {
        try {
            val response = apiService.addPost(post)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                println(response)
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromDto(body))
            }


        } catch (e: IOException) {
            throw NetworkError
        }
    }

}