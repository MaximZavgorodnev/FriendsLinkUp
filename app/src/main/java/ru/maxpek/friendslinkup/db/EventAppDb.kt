package ru.maxpek.friendslinkup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.maxpek.friendslinkup.dao.*
import ru.maxpek.friendslinkup.entity.EventEntity
import ru.maxpek.friendslinkup.entity.EventRemoteKeyEntity


@Database(
    entities = [EventEntity::class, EventRemoteKeyEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    Converters::class, CoordinatesConverter::class, EventTypeConverters::class,
    ConvertersListIds::class
)
abstract class EventAppDb : RoomDatabase() {
    abstract fun eventDao(): EventDao
    abstract fun eventRemoteKeyDao(): EventRemoteKeyDao
}