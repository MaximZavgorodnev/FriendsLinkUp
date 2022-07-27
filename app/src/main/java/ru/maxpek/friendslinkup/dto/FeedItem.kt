package ru.maxpek.friendslinkup.dto


import android.graphics.Bitmap
import android.media.Image
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.enumeration.TypeEvent


sealed class FeedItem{
    abstract val id: Long
}

data class Ad(
    override val id: Long,
    val url: String,
    val image: String,
) : FeedItem()

data class Post(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    val coordinates: Coordinates?,
    val link: String,
    val likeOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment?,
) : FeedItem()

data class Job (
    override val id: Long,
    val name: String,
    val position: String,
    val start: String,
    val finish: String,
    val link: String,
) : FeedItem()

data class Event(
    override val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val datetime: String,
    val published: Long,
    val coordinates: Coordinates?,
    val type: TypeEvent,
    val likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    val speakerIds: List<Int>,
    val participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val link: String,
    val attachment: Attachment?,
) : FeedItem()


data class UserResponse (
    val login: String,
    val password: String,
)

data class UserRequested (
    val id: Long,
    val login: String,
    val name: String,
    val avatar: String,
)

data class UserRegistration (
    val login: String,
    val password: String,
    val name: String,
    val file: Bitmap
)


data class ListIds (
    val list: List<Int>
)

data class Attachment (
    val url: String,
    val typeAttachment: AttachmentType,
)

data class Coordinates (
    val latitude: Long,
    val longitude: Long
)






