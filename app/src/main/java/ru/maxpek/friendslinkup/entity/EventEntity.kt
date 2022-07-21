package ru.maxpek.friendslinkup.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.Event
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
    @Embedded
    val coordinates: CoordinatesEmbeddable?,
    val type: TypeEvent,
    @Embedded
    var likeOwnerIds: List<Int>,
    val likedByMe: Boolean,
    @Embedded
    var speakerIds: List<Int>,
    @Embedded
    var participantsIds: List<Int>,
    val participatedByMe: Boolean,
    val link: String,
    @Embedded
    var attachment: AttachmentEmbeddable?,
    ) {
        fun toDto() = Event(id, authorId, author, authorAvatar, content, datetime,
            published, coordinates?.toDto(),type, likeOwnerIds,likedByMe, speakerIds,
            participantsIds, participatedByMe, link, attachment?.toDto() )

        companion object {
            fun fromDto(dto: Event) =
                EventEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                    dto.content, dto.datetime, dto.published, CoordinatesEmbeddable.fromDto(dto.coordinates),
                    dto.type, dto.likeOwnerIds, dto.likedByMe, dto.speakerIds, dto.participantsIds, dto.participatedByMe,
                    dto.link, AttachmentEmbeddable.fromDto(dto.attachment))

            fun fromDtoFlow(dto: Event) =
                EventEntity(dto.id, dto.authorId, dto.author, dto.authorAvatar,
                    dto.content, dto.datetime, dto.published, CoordinatesEmbeddable.fromDto(dto.coordinates),
                    dto.type, dto.likeOwnerIds, dto.likedByMe, dto.speakerIds, dto.participantsIds, dto.participatedByMe,
                    dto.link, AttachmentEmbeddable.fromDto(dto.attachment))
        }
    }

    fun List<EventEntity>.toDto(): List<Event> = map(EventEntity::toDto)
    fun List<Event>.toEntity(): List<EventEntity> = map(EventEntity::fromDto)
    fun List<Event>.toEntityFlow(): List<EventEntity> = map(EventEntity::fromDtoFlow)