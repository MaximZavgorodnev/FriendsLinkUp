package ru.maxpek.friendslinkup.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.*
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
    val coordinates: Coordinates?,
    val link: String,
    val likeOwnerIds: LikeOwnerIdsHolder,
    val mentionIds: MentionIdsHolder,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbeddable?,
) {
//    constructor() : this(0, 0, "", "", "", 0,
//        null,"", listOf<Int>(), listOf<Int>(),false,false, null)

    fun toDto() = Post(id, authorId, author, authorAvatar, content,
        published,coordinates,link, likeOwnerIds.likeOwnerIdsList,
        mentionIds.mentionIdsList, mentionedMe, likedByMe, attachment?.toDto() )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.content, dto.published,dto.coordinates,
                dto.link, LikeOwnerIdsHolder(dto.likeOwnerIds),  MentionIdsHolder(dto.mentionIds), dto.mentionedMe,
                dto.likedByMe, AttachmentEmbeddable.fromDto(dto.attachment))

        fun fromDtoFlow(dto: Post) =
            PostEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                dto.content, dto.published,dto.coordinates,
                dto.link, LikeOwnerIdsHolder(dto.likeOwnerIds),  MentionIdsHolder(dto.mentionIds), dto.mentionedMe,
                dto.likedByMe, AttachmentEmbeddable.fromDto(dto.attachment))
    }
}


data class AttachmentEmbeddable(
    var url: String,
    var typeAttach: AttachmentType,
) {
    fun toDto() = Attachment(url, typeAttach)

    companion object {
        fun fromDto(dto: Attachment?) = dto?.let {
            AttachmentEmbeddable(it.url, it.typeAttachment)
        }
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map(PostEntity::fromDto)
fun List<Post>.toEntityFlow(): List<PostEntity> = map(PostEntity::fromDtoFlow)