package ru.maxpek.friendslinkup.repository.newPost

import androidx.lifecycle.MutableLiveData
import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.MediaResponse
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.enumeration.AttachmentType

interface NewPostRepository {
    val dataUsers: MutableLiveData<List<UserRequested>>
    val dataAttachment: MutableLiveData<Attachment>
    suspend fun loadUsers()
    suspend fun addPictureToThePost(attachmentType: AttachmentType, image: MultipartBody.Part)
    suspend fun addPost(post: PostCreateRequest)
}