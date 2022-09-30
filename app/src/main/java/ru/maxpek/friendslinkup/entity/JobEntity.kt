package ru.maxpek.friendslinkup.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.maxpek.friendslinkup.dto.JobResponse

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val position: String,
    val start: String,
    val finish: String,
    val link: String,
) {
    fun toDto() = JobResponse(id, name, position, start, finish, link)

    companion object {
        fun fromDto(dto: JobResponse) =
            JobEntity(dto.id, dto.name, dto.position, dto.start, dto.finish, dto.link)
    }
}


fun List<JobResponse>.toEntity(): List<JobEntity> = map(JobEntity::fromDto)