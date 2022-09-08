package ru.maxpek.friendslinkup.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.*
import ru.maxpek.friendslinkup.enumeration.AttachmentType

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,
    val coords: Coordinates?,
    val link: String?,
    val likeOwnerIds: ListIds,
    val mentionIds: ListIds,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    val ownerByMe: Boolean,
//    val users: ListUserPreview
) {
    fun toDto() = PostResponse(id, authorId, author, authorAvatar, authorJob, content,
        published,coords,link, likeOwnerIds.list, mentionIds.list, mentionedMe,
        likedByMe, attachment?.toDto(), ownerByMe )

    companion object {
        fun fromDto(dto: PostResponse) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.authorJob,
                dto.content, dto.published,dto.coords,
                dto.link, ListIds(dto.likeOwnerIds),  ListIds(dto.mentionIds), dto.mentionedMe,
                dto.likedByMe, AttachmentEmbeddable.fromDto(dto.attachment), dto.ownerByMe
            )

        fun fromDtoFlow(dto: PostResponse) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.authorJob,
                dto.content, dto.published,dto.coords,
                dto.link, ListIds(dto.likeOwnerIds),  ListIds(dto.mentionIds), dto.mentionedMe,
                dto.likedByMe, AttachmentEmbeddable.fromDto(dto.attachment), dto.ownerByMe,
               )
    }
}


data class AttachmentEmbeddable(
    var url: String,
    var typeAttach: AttachmentType,
) {
    fun toDto() = Attachment(url, typeAttach)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.type)
        }
    }
}

fun List<PostEntity>.toDto(): List<PostResponse> = map(PostEntity::toDto)
fun List<PostResponse>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
fun List<PostResponse>.toEntityFlow(): List<PostEntity> = map(PostEntity::fromDtoFlow)