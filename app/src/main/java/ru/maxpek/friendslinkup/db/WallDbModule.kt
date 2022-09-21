package ru.maxpek.friendslinkup.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.maxpek.friendslinkup.dao.PostDao
import ru.maxpek.friendslinkup.dao.PostRemoteKeyDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class WallDbModule {
    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext context: Context
    ): WallPostAppDb = Room.databaseBuilder(context, WallPostAppDb::class.java, "wallPosts.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideWallPostAppDb(appDb: WallPostAppDb): PostDao = appDb.postDao()

    @Provides
    fun providePostRemoteKeyDao(appDb: WallPostAppDb): PostRemoteKeyDao = appDb.postRemoteKeyDao()
}