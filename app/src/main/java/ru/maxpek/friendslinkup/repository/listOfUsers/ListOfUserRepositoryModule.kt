package ru.maxpek.friendslinkup.repository.listOfUsers

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface ListOfUserRepositoryModule {
    @Binds
    @Singleton
    fun bindListOfUserRepository(impl: ListOfUserRepositoryImpl): ListOfUserRepository
}