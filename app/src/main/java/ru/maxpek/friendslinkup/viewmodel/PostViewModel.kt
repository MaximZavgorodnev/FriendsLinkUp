package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.LiveData
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
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.repository.post.PostRepository
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repositoryPost: PostRepository,
    appAuth: AppAuth
): ViewModel() {

    val dataUserMentions: LiveData<List<UserRequested>> = repositoryPost.dataUsersMentions
    val data: Flow<PagingData<PostResponse>> = appAuth
        .authStateFlow
        .flatMapLatest { (myId, _) ->
            val cached = repositoryPost.data.cachedIn(viewModelScope)
            cached.map { pagingData ->
                pagingData.map {
                    it.copy(ownedByMe = it.authorId.toLong() == myId)
                }
            }
        }
}
