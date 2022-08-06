package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.newPost.NewPostRepository
import javax.inject.Inject

//val edited = Post(
//    id = 0,
//    authorId = 0,
//    author = "",
//    authorAvatar = "",
//    content = "",
//    published = 0,
//    coordinates = null,
//    link = null,
//val likeOwnerIds: List<Int>,
//val mentionIds: List<Int>,
//val mentionedMe: Boolean,
//val likedByMe: Boolean,
//val attachment: Attachment?,
//)

@HiltViewModel
class NewPostViewModel@Inject constructor(
    private val repositoryListOfUser : NewPostRepository): ViewModel() {

//    val newPost : MutableLiveData<Post()>




    var listChecked = listOf<Int>()



    val data: MutableLiveData<List<UserRequested>> = repositoryListOfUser.dataUsers

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun getUsers() {
        viewModelScope.launch {
            try {
                repositoryListOfUser.loadUsers()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }



}