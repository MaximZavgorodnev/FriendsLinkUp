package ru.maxpek.friendslinkup.repository.newEvent

import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.EventCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.enumeration.AttachmentType

interface NewEventRepository {
    suspend fun loadUsers(): List<UserRequested>
    suspend fun addPictureToTheEvent(attachmentType: AttachmentType, image: MultipartBody.Part): Attachment
    suspend fun addEvent(event: EventCreateRequest)
    suspend fun getEvent(id: Int): EventCreateRequest
    suspend fun getUser(id: Int): UserRequested
}