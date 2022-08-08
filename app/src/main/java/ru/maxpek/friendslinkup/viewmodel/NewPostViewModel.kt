package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.newPost.NewPostRepository
import javax.inject.Inject

val edited = PostCreateRequest(
    id = 0,
    content = "",
    coords = null,
    link = null,
    attachment = null,
    mentionIds = listOf())

@HiltViewModel
class NewPostViewModel@Inject constructor(
    private val repositoryListOfUser : NewPostRepository): ViewModel() {

    private val _newPost = MutableLiveData(edited)
    val newPost: LiveData<PostCreateRequest>
        get() = _newPost

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

    fun isChecked(id: Int){
        data.value?.forEach {
            if (it.id == id) {
                it.checked = true
            }
        }
    }

    fun unChecked(id: Int){
        data.value?.forEach {
            if (it.id == id) {
                it.checked = false
            }
        }
    }

    fun addContent(){

    }

    fun addCoords(){

    }

    fun addLink(){

    }

    fun addAttachment(){

    }

    fun addMentionIds(){
        val listChecked = mutableListOf<Int>()
        data.value?.forEach { user ->
            if (user.checked){
                listChecked.add(user.id)
            }
        }
        _newPost.value = _newPost.value?.copy(mentionIds = listChecked)
    }

}