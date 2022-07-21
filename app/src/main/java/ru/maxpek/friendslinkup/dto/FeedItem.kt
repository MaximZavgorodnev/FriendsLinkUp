package ru.maxpek.friendslinkup.dto

import android.os.Bundle
import com.yandex.mapkit.geometry.Point
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.enumeration.TypeEvent
import ru.maxpek.friendslinkup.util.PointArg
//import ru.maxpek.friendslinkup.util.StringArg

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
    val coordinates: Coordinates? = null,
    val link: String,
    val likeOwnerIds: List<Int>,
    val mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    val attachment: Attachment? = null,
) : FeedItem()

data class Job(
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
    val participatedByMe: Boolean = false,
    val link: String,
    val attachment: Attachment? = null,
) : FeedItem()


data class Attachment(
    val url: String,
    val type: AttachmentType,
)

data class Coordinates(
    val lat: Long,
    val long: Long
)

data class User(
    val id: Long,
    val login: String,
    val password: String,
    val name: String,
    val avatar: String,
)





