package ru.maxpek.friendslinkup.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.maxpek.friendslinkup.dao.userWall.UserWallJobDao
import ru.maxpek.friendslinkup.dao.userWall.UserWallRemoteKeyDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UserWallDbModule {

    @Singleton
    @Provides
    fun provideAppDb(
        @ApplicationContext context: Context
    ): UserWallAppDb = Room.databaseBuilder(context, UserWallAppDb::class.java, "userWall.db")
        .fallbackToDestructiveMigration()
        .build()


    @Provides
    fun userWallJobDao(appDb: UserWallAppDb): UserWallJobDao = appDb.userWallJobDao()

    @Provides
    fun userWallRemoteKeyDao(appDb: UserWallAppDb): UserWallRemoteKeyDao = appDb.userWallRemoteKeyDao()
}
