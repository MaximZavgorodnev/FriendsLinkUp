package ru.maxpek.friendslinkup.repository.event

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.maxpek.friendslinkup.dto.EventResponse
import ru.maxpek.friendslinkup.dto.UserRequested

interface EventRepository {
    val data: Flow<PagingData<EventResponse>>
    val dataUsersSpeakers: MutableLiveData<List<UserRequested>>
    suspend fun loadUsersSpeakers(list: List<Int>)
    suspend fun removeById(id: Int)
    suspend fun likeById(id: Int): EventResponse
    suspend fun disLikeById(id: Int): EventResponse
    suspend fun participateInEvent(id: Int): EventResponse
    suspend fun doNotParticipateInEvent(id: Int): EventResponse
    suspend fun getEvent(id: Int): EventResponse
}