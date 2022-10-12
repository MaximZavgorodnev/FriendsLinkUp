package ru.maxpek.friendslinkup.dto


import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.enumeration.TypeEvent


sealed class FeedItem {
    abstract val id: Int
}

data class PostCreateRequest(
    val id: Int,
    val content: String,
    val coords: Coordinates?,
    val link: String?,
    val attachment: Attachment?,
    val mentionIds: List<Int>,
)

data class PostResponse(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coordinates?,
    val link: String?,
    val likeOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
    val ownerByMe: Boolean = false,

    )

data class EventCreateRequest(
    val id: Int,
    val content: String,
    val datetime: String?,
    val coords: Coordinates?,
    val type: TypeEvent?,
    val attachment: Attachment?,
    val link: String?,
    val speakerIds: List<Int>?,

    )

data class EventResponse(
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates?,
    val type: TypeEvent,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val attachment: Attachment?,
    val link: String?,
    val ownerByMe: Boolean
)

data class Job(
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String?,
    val link: String?,
)

data class UserResponse(
    val login: String,
    val password: String,
)

data class UserRequested(
    val id: Int = 0,
    val login: String = "",
    val name: String = "",
    val avatar: String = "",
    var checked: Boolean = false
)

data class UserRegistration(
    val login: String,
    val password: String,
    val name: String,
    val file: MultipartBody.Part?
)

data class ListIds(
    val list: List<Int>
)

data class Attachment(
    val url: String,
    val type: AttachmentType,
)

data class Coordinates(
    val lat: String,
    val long: String
)

data class MediaResponse(
    val url: String
)






