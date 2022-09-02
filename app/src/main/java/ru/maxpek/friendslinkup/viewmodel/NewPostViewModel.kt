package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.*
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.dto.Attachment
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.model.ErrorLive
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.newPost.NewPostRepository
import ru.maxpek.friendslinkup.repository.newPost.attachment
import javax.inject.Inject

val edited = PostCreateRequest(
    id = 0,
    content = "",
    coords = Coordinates("0", "0"),
    link = null,
    attachment = null,
    mentionIds = listOf())

@ExperimentalCoroutinesApi
@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repositoryListOfUser : NewPostRepository): ViewModel() {

    private val _newPost = MutableLiveData(edited)
    val newPost: LiveData<PostCreateRequest>
        get() = _newPost

    val data: MutableLiveData<List<UserRequested>> = repositoryListOfUser.dataUsers
    private val mentions = mutableListOf<UserRequested>()
    val mentionsLive : LiveData<List<UserRequested>> = MutableLiveData(mentions)
    val dataAttachment = repositoryListOfUser.dataAttachment



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

    fun addPost(content: String){
        _newPost.value = _newPost.value?.copy(content = content)
        val post = _newPost.value!!
        println(post)
        viewModelScope.launch {
            try {
                repositoryListOfUser.addPost(post)
                _dataState.value = FeedModelState(error = false)
                println(post)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addCoords(point: Point){
        point.latitude.
        val coordinates = Coordinates(point.latitude.toString(), point.longitude.toString())
        _newPost.value = _newPost.value?.copy(coords = coordinates)
    }

    fun addLink(link: String){
        if (link != ""){
            _newPost.value = _newPost.value?.copy(link = link)
        } else {
            _newPost.value = _newPost.value?.copy(link = null)
        }
    }

    fun addPictureToThePost(image: MultipartBody.Part){
        viewModelScope.launch {
            try {
                repositoryListOfUser.addPictureToThePost(AttachmentType.IMAGE, image)
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addAttachment(){
        _newPost.value = _newPost.value?.copy(attachment = dataAttachment.value)
    }

    fun addMentionIds(){
        mentions.clear()
        val listChecked = mutableListOf<Int>()
        data.value?.forEach { user ->
            if (user.checked){
                listChecked.add(user.id)
                mentions.add(user)
            }
        }
        _newPost.value = _newPost.value?.copy(mentionIds = listChecked)
    }

}