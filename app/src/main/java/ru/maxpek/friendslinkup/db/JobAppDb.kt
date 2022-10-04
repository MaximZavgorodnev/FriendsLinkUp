package ru.maxpek.friendslinkup.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.maxpek.friendslinkup.dao.Converters
import ru.maxpek.friendslinkup.dao.JobDao
import ru.maxpek.friendslinkup.entity.JobEntity

@Database(entities = [JobEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class JobAppDb : RoomDatabase() {
    abstract fun jobDao(): JobDao
}