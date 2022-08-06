package ru.maxpek.friendslinkup.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.*
import ru.maxpek.friendslinkup.enumeration.TypeEvent

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val authorId: Int,
    val author: String,
    val authorAvatar: String,
    val authorJob: String?,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates?,
    val type: TypeEvent,
    val likeOwnerIds: ListIds,
    val likedByMe: Boolean,
    val speakerIds: ListIds,
    val participantsIds: ListIds,
    val participatedByMe: Boolean,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    val link: String,
    val ownerByMe: Boolean,
    val users: ListUserPreview

    ) {

    fun toDto() = EventResponse(id, authorId, author, authorAvatar, authorJob, content,
        datetime, published, coords,type, likeOwnerIds.list,likedByMe, speakerIds.list,
            participantsIds.list, participatedByMe, attachment?.toDto(), link, ownerByMe,
        users.list)

        companion object {
            fun fromDto(dto: EventResponse) =
                EventEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.authorJob,
                    dto.content, dto.datetime, dto.published, dto.coords,
                    dto.type, ListIds(dto.likeOwnerIds), dto.likedByMe,
                    ListIds(dto.speakerIds), ListIds(dto.participantsIds),
                    dto.participatedByMe, AttachmentEmbeddable.fromDto(dto.attachment),
                    dto.link, dto.ownerByMe, ListUserPreview(dto.users))

            fun fromDtoFlow(dto: EventResponse) =
                EventEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar, dto.authorJob,
                    dto.content, dto.datetime, dto.published, dto.coords,
                    dto.type, ListIds(dto.likeOwnerIds), dto.likedByMe,
                    ListIds(dto.speakerIds), ListIds(dto.participantsIds),
                    dto.participatedByMe, AttachmentEmbeddable.fromDto(dto.attachment),
                    dto.link, dto.ownerByMe, ListUserPreview(dto.users))
        }
    }

    fun List<EventEntity>.toDto(): List<EventResponse> = map(EventEntity::toDto)
    fun List<EventResponse>.toEntity(): List<EventEntity> = map(EventEntity::fromDto)
    fun List<EventResponse>.toEntityFlow(): List<EventEntity> = map(EventEntity::fromDtoFlow)