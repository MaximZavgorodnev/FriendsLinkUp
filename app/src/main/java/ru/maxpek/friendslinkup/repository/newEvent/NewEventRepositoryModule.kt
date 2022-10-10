package ru.maxpek.friendslinkup.repository.newEvent

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface NewEventRepositoryModule {
    @Binds
    @Singleton
    fun bindNewEventRepository(impl: NewEventRepositoryImpl): NewEventRepository
}