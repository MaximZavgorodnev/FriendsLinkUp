package ru.maxpek.friendslinkup.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.Post
import ru.maxpek.friendslinkup.enumeration.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: Long,
    @Embedded
    val coordinates: CoordinatesEmbeddable?,
    val link: String,
    @Embedded
    var likeOwnerIds: List<Int>,
    @Embedded
    var mentionIds: List<Int>,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    var attachment: AttachmentEmbeddable?,
) {
    fun toDto() = Post(id, authorId, author, authorAvatar, content,
        published,coordinates?.toDto(),link, likeOwnerIds,
        mentionIds, mentionedMe, likedByMe, attachment?.toDto() )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.content, dto.published,CoordinatesEmbeddable.fromDto(dto.coordinates),
                dto.link, dto.likeOwnerIds,  dto.mentionIds, dto.mentionedMe,
                dto.likedByMe, AttachmentEmbeddable.fromDto(dto.attachment))

        fun fromDtoFlow(dto: Post) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.content, dto.published,CoordinatesEmbeddable.fromDto(dto.coordinates),
                dto.link, dto.likeOwnerIds,  dto.mentionIds, dto.mentionedMe,
                dto.likedByMe, AttachmentEmbeddable.fromDto(dto.attachment))
    }
}

data class CoordinatesEmbeddable(
    val lat: Long,
    val long: Long
) {
    fun toDto() = Coordinates(lat, long)

    companion object {
        fun fromDto(dto: Coordinates?) = dto?.let {
            CoordinatesEmbeddable(it.lat, it.long)
        }
    }
}


data class AttachmentEmbeddable(
    var url: String,
    var type: AttachmentType,
) {
    fun toDto() = Attachment(url, type)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
fun List<Post>.toEntityFlow(): List<PostEntity> = map(PostEntity::fromDtoFlow)