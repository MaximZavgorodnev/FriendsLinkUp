package ru.maxpek.friendslinkup.repository.newPost

import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.MediaResponse
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.enumeration.AttachmentType

interface NewPostRepository {
    suspend fun loadUsers(): List<UserRequested>
    suspend fun addPictureToThePost(attachmentType: AttachmentType, image: MultipartBody.Part): Attachment
    suspend fun addPost(post: PostCreateRequest)
    suspend fun getPost(id: Int): PostCreateRequest
    suspend fun getUser(id: Int): UserRequested
}