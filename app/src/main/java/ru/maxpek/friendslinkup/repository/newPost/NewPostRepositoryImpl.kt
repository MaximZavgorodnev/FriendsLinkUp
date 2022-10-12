package ru.maxpek.friendslinkup.repository.newPost

import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import java.io.IOException
import javax.inject.Inject


class NewPostRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val dao: PostDao
) : NewPostRepository {

    override suspend fun loadUsers(): List<UserRequested> {
        val usersList: List<UserRequested>
        try {
            val response = apiService.getUsers()
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            usersList = response.body() ?: throw ApiError(response.code(), response.message())
            return usersList
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun addPictureToThePost(
        attachmentType: AttachmentType,
        image: MultipartBody.Part
    ): Attachment {

        try {
            val response = apiService.addMultimedia(image)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            }
            val mediaResponse =
                response.body() ?: throw ApiError(response.code(), response.message())
            val attachment = Attachment(mediaResponse.url, attachmentType)
            return attachment
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
                val body = response.body() ?: throw ApiError(response.code(), response.message())
                dao.insert(PostEntity.fromDto(body))
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }

    override suspend fun getPost(id: Int): PostCreateRequest {
        try {
            val response = apiService.getPost(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                val body =
                    response.body() ?: throw ApiError(response.code(), response.message())
                return PostCreateRequest(
                    id = body.id,
                    content = body.content,
                    coords = body.coords,
                    link = body.link,
                    attachment = body.attachment,
                    mentionIds = body.mentionIds
                )
            }
        } catch (e: IOException) {
            throw NetworkError
        }

    }

    override suspend fun getUser(id: Int): UserRequested {
        try {
            val response = apiService.getUser(id)
            if (!response.isSuccessful) {
                throw ApiError(response.code(), response.message())
            } else {
                return response.body() ?: throw ApiError(response.code(), response.message())
            }
        } catch (e: IOException) {
            throw NetworkError
        }
    }

}