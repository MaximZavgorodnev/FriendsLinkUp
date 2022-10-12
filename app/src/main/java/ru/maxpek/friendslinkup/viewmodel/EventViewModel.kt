package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dto.EventResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.event.EventRepository
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class EventViewModel @Inject constructor(
    private val repositoryEvent: EventRepository,
    appAuth: AppAuth
) : ViewModel() {

    var lastAction: ActionType? = null
    var lastId = 0
    var errorCounter = 0
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    private val _eventResponse = MutableLiveData<EventResponse>()
    val eventResponse: LiveData<EventResponse>
        get() = _eventResponse

    val dataUserSpeakers: LiveData<List<UserRequested>> = repositoryEvent.dataUsersSpeakers

    val data: Flow<PagingData<EventResponse>> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            val cached = repositoryEvent.data.cachedIn(viewModelScope)
            cached.map { pagingData ->
                pagingData.map {
                    it.copy(ownerByMe = it.authorId.toLong() == myId)
                }
            }
        }

    fun loadUsersSpeakers(mentionIds: List<Int>) {
        viewModelScope.launch {
            try {
                repositoryEvent.loadUsersSpeakers(mentionIds)
                _dataState.value = FeedModelState(loading = false)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(loading = true)
            }
        }
    }

    fun removeById(id: Int) {
        lastAction = ActionType.REMOVE
        viewModelScope.launch {
            try {
                repositoryEvent.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun likeById(id: Int) {
        lastAction = ActionType.LIKE
        lastId = id
        viewModelScope.launch {
            try {
                _eventResponse.postValue(repositoryEvent.likeById(id))
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun disLikeById(id: Int) {
        lastAction = ActionType.DISLIKE
        lastId = id
        viewModelScope.launch {
            try {
                _eventResponse.postValue(repositoryEvent.disLikeById(id))
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun participateInEvent(id: Int) {
        lastAction = ActionType.PARTICIPATE
        lastId = id
        viewModelScope.launch {
            try {
                _eventResponse.postValue(repositoryEvent.participateInEvent(id))
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun doNotParticipateInEvent(id: Int) {
        lastAction = ActionType.DONOTPARTICIPATE
        lastId = id
        viewModelScope.launch {
            try {
                _eventResponse.postValue(repositoryEvent.doNotParticipateInEvent(id))
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun goToUserParticipateInEvent(users: List<Int>) {
        viewModelScope.launch {
            try {
                repositoryEvent.loadUsersSpeakers(users)
                _dataState.value = FeedModelState(loading = false)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(loading = true)
            }
        }
    }

    fun getEvent(id: Int) {
        viewModelScope.launch {
            try {
                _eventResponse.postValue(repositoryEvent.getEvent(id))
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun retry() {
        if (errorCounter == 3 || errorCounter > 3) {
            _dataState.value = FeedModelState(loading = true)
        } else {
            when (lastAction) {
                ActionType.LIKE -> retryLikeById()
                ActionType.DISLIKE -> retryDisLikeById()
                ActionType.REMOVE -> retryRemove()
                ActionType.PARTICIPATE -> retryParticipateInEvent()
                ActionType.DONOTPARTICIPATE -> retryDoNotParticipateInEvent()
                null -> {}
            }
        }
    }

    fun retryLikeById() {
        lastId.let {
            likeById(it)
        }
        errorCounter++
    }

    fun retryDisLikeById() {
        lastId.let {
            disLikeById(it)
        }
        errorCounter++
    }

    fun retryRemove() {
        lastId.let {
            removeById(it)
        }
        errorCounter++
    }

    fun retryParticipateInEvent() {
        lastId.let {
            participateInEvent(it)
        }
        errorCounter++
    }

    fun retryDoNotParticipateInEvent() {
        lastId.let {
            doNotParticipateInEvent(it)
        }
        errorCounter++
    }


}
