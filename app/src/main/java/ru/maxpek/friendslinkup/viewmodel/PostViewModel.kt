package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.repository.post.PostRepository
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltViewModel
class PostViewModel @Inject constructor(
    private val repositoryPost: PostRepository): ViewModel() {
        val dataUserMentions: LiveData<List<UserRequested>> = repositoryPost

}