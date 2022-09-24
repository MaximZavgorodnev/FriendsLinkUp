package ru.maxpek.friendslinkup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.maxpek.friendslinkup.dao.*
import ru.maxpek.friendslinkup.dao.myWall.MyWallPostDao
import ru.maxpek.friendslinkup.dao.myWall.MyWallRemoteKeyDao
import ru.maxpek.friendslinkup.entity.PostEntity
import ru.maxpek.friendslinkup.entity.PostRemoteKeyEntity

@Database(entities = [PostEntity::class, PostRemoteKeyEntity::class], version = 3, exportSchema = false)
@TypeConverters(
    Converters::class, CoordinatesConverter::class,
    ConvertersListIds::class)
abstract class WallPostAppDb : RoomDatabase() {
    abstract fun myWallPostDao(): MyWallPostDao
    abstract fun myWallPostRemoteKeyDao(): MyWallRemoteKeyDao
}