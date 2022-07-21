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
class PostDbModule {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext context: Context
    ): PostAppDb = Room.databaseBuilder(context, PostAppDb::class.java, "posts.db")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun providePostDao(appDb: PostAppDb): PostDao = appDb.postDao()

    @Provides
    fun providePostRemoteKeyDao(appDb: PostAppDb): PostRemoteKeyDao = appDb.postRemoteKeyDao()
}