package ru.maxpek.friendslinkup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.maxpek.friendslinkup.dao.Converters
import ru.maxpek.friendslinkup.dao.JobDao
import ru.maxpek.friendslinkup.dao.JobRemoteKeyDao
import ru.maxpek.friendslinkup.entity.JobEntity
import ru.maxpek.friendslinkup.entity.JobRemoteKeyEntity

@Database(entities = [JobEntity::class, JobRemoteKeyEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class JobAppDb : RoomDatabase() {
    abstract fun jobDao(): JobDao
    abstract fun jobRemoteKeyDao(): JobRemoteKeyDao
}