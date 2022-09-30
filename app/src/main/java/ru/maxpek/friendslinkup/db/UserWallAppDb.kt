package ru.maxpek.friendslinkup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.maxpek.friendslinkup.dao.userWall.UserWallJobDao
import ru.maxpek.friendslinkup.dao.userWall.UserWallRemoteKeyDao
import ru.maxpek.friendslinkup.entity.JobEntity
import ru.maxpek.friendslinkup.entity.JobRemoteKeyEntity


@Database(entities = [JobEntity::class, JobRemoteKeyEntity::class], version = 1, exportSchema = false)
abstract class UserWallAppDb : RoomDatabase() {
    abstract fun userWallJobDao(): UserWallJobDao
    abstract fun userWallRemoteKeyDao(): UserWallRemoteKeyDao
}