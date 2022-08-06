package ru.maxpek.friendslinkup.repository.newPost

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface NewPostRepositoryModule {
    @Binds
    @Singleton
    fun bindListOfUserRepository(impl: NewPostRepositoryImpl): NewPostRepository
}