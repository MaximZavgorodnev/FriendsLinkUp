package ru.maxpek.friendslinkup.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class JobEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val position: String,
    val start: String,
    val finish: String,
    val link: String,
)