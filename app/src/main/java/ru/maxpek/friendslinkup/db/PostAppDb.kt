package ru.maxpek.friendslinkup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.maxpek.friendslinkup.dao.Converters
import ru.maxpek.friendslinkup.dao.CoordinatesConverter
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dao.PostRemoteKeyDao
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.entity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class, PostRemoteKeyEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class, CoordinatesConverter::class,)
abstract class PostAppDb : RoomDatabase() {
    abstract fun postDao(): PostDao
    abstract fun postRemoteKeyDao(): PostRemoteKeyDao
}