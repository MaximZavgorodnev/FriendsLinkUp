package ru.maxpek.friendslinkup.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.maxpek.friendslinkup.dao.EventDao
import ru.maxpek.friendslinkup.dao.EventRemoteKeyDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class EventDbModule {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext context: Context
    ): EventAppDb = Room.databaseBuilder(context, EventAppDb::class.java, "events.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideEventDao(appDb: EventAppDb): EventDao = appDb.eventDao()

    @Provides
    fun provideEventRemoteKeyDao(appDb: EventAppDb): EventRemoteKeyDao = appDb.eventRemoteKeyDao()
}