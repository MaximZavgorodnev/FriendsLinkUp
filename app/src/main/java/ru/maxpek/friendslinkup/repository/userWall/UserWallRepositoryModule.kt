package ru.maxpek.friendslinkup.repository.userWall

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface UserWallRepositoryModule {
    @Binds
    @Singleton
    fun bindUserWallRepository(impl: UserWallRepositoryImpl): UserWallRepository
}