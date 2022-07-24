package ru.maxpek.friendslinkup.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.*
import ru.maxpek.friendslinkup.enumeration.TypeEvent

@Entity
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val datetime: String,
    val published: Long,
    val coordinates: Coordinates?,
    val type: TypeEvent,
    val likeOwnerIds: ListIds,
    val likedByMe: Boolean,
    val speakerIds: ListIds,
    val participantsIds: ListIds,
    val participatedByMe: Boolean,
    val link: String,
    @Embedded
    val attachment: AttachmentEmbeddable?,
    ) {

    fun toDto() = Event(id, authorId, author, authorAvatar, content, datetime,
            published, coordinates,type, likeOwnerIds.list,likedByMe, speakerIds.list,
            participantsIds.list, participatedByMe, link, attachment?.toDto() )

        companion object {
            fun fromDto(dto: Event) =
                EventEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                    dto.content, dto.datetime, dto.published, dto.coordinates,
                    dto.type, ListIds(dto.likeOwnerIds), dto.likedByMe,
                    ListIds(dto.speakerIds), ListIds(dto.participantsIds),
                    dto.participatedByMe, dto.link, AttachmentEmbeddable.fromDto(dto.attachment))

            fun fromDtoFlow(dto: Event) =
                EventEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                    dto.content, dto.datetime, dto.published, dto.coordinates,
                    dto.type, ListIds(dto.likeOwnerIds), dto.likedByMe,
                    ListIds(dto.speakerIds), ListIds(dto.participantsIds),
                    dto.participatedByMe, dto.link, AttachmentEmbeddable.fromDto(dto.attachment))
        }
    }

    fun List<EventEntity>.toDto(): List<Event> = map(EventEntity::toDto)
    fun List<Event>.toEntity(): List<EventEntity> = map(EventEntity::fromDto)
    fun List<Event>.toEntityFlow(): List<EventEntity> = map(EventEntity::fromDtoFlow)