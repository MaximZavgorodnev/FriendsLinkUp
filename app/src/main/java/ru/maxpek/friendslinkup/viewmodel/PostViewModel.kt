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
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.NetworkError
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.post.PostRepository
import ru.maxpek.friendslinkup.viewmodel.ActionType.*
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repositoryPost: PostRepository,
    appAuth: AppAuth
): ViewModel() {

    var lastAction: ActionType? = null
    var lastId = 0
    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val dataUserMentions: LiveData<List<UserRequested>> = repositoryPost.dataUsersMentions
    val data: Flow<PagingData<PostResponse>> = appAuth
            .authStateFlow
            .flatMapLatest { (myId, _) ->
                val cached = repositoryPost.data.cachedIn(viewModelScope)
                cached.map { pagingData ->
                    pagingData.map {
                        it.copy(ownerByMe = it.authorId.toLong() == myId )
                    }
                }
            }

    fun removeById(id: Int) {
        lastAction = REMOVE
        viewModelScope.launch {
            try {
                repositoryPost.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun likeById(id: Int) {
        lastAction = LIKE
        lastId = id
        viewModelScope.launch {
            try {
                repositoryPost.likeById(id)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun disLikeById(id: Int) {
        lastAction = DISLIKE
        lastId = id
        viewModelScope.launch {
            try {
                repositoryPost.disLikeById(id)


            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun retry(){
        when (lastAction){
            LIKE -> retryLikeById()
            DISLIKE -> retryDisLikeById()
            REMOVE -> retryRemove()
            null -> TODO()
        }
    }

    fun retryLikeById(){
        lastId.let{
            likeById(it)}
    }

    fun retryDisLikeById(){
        lastId.let{
            disLikeById(it)}
    }

    fun retryRemove(){
        lastId.let{
            removeById(it)
        }
    }
}



enum class ActionType{
    LIKE,
    DISLIKE,
    REMOVE
}
