package ru.maxpek.friendslinkup.repository.myWall.post

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.maxpek.friendslinkup.repository.post.PostRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface MyWallPostRepositoryModuleIe {
    @Binds
    @Singleton
    fun bindMyWallPostRepository(impl: MyWallPostRepositoryImpl): PostRepository
}