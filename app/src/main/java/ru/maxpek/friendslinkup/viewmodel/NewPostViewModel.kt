package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.*
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.PostCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.newPost.NewPostRepository
import ru.maxpek.friendslinkup.util.SingleLiveEvent
import javax.inject.Inject
import kotlin.math.roundToInt


val editedPost = PostCreateRequest(
    id = 0,
    content = "",
    coords = null,
    link = null,
    attachment = null,
    mentionIds = listOf())

val mentions = mutableListOf<UserRequested>()
@ExperimentalCoroutinesApi
@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repository : NewPostRepository): ViewModel() {

    var inJob = false

    val newPost: MutableLiveData<PostCreateRequest> = MutableLiveData(editedPost)

    val data: MutableLiveData<List<UserRequested>> = MutableLiveData()

    val mentionsLive : MutableLiveData<MutableList<UserRequested>> = MutableLiveData()

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun getPost(id: Int){

        mentionsLive.postValue(mentions)
        viewModelScope.launch {
            try {
                newPost.value = repository.getPost(id)
                newPost.value?.mentionIds?.forEach {
                    mentionsLive.value!!.add(repository.getUser(it))
                }
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getUsers() {
        mentionsLive.postValue(mentions)
        viewModelScope.launch {
            try {
                data.value = repository.loadUsers()
                data.value?.forEach { user ->
                    newPost.value?.mentionIds?.forEach {
                        if (user.id == it) {
                            user.checked = true
                        }
                    }
                }
                _dataState.value = FeedModelState(error = false)
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addMentionIds(){
        mentionsLive.postValue(mentions)
        val listChecked = mutableListOf<Int>()
        val mentionsUserList = mutableListOf<UserRequested>()
        data.value?.forEach { user ->
            if (user.checked){
                listChecked.add(user.id)
                mentionsUserList.add(user)
            }
        }
        mentionsLive.postValue(mentionsUserList)
        newPost.value = newPost.value?.copy(mentionIds = listChecked)
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
        newPost.value = newPost.value?.copy(content = content)
        val post = newPost.value!!
        viewModelScope.launch {
            try {
                repository.addPost(post)
                _dataState.value = FeedModelState(error = false)
                deleteEditPost()
                _postCreated.value = Unit
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addCoords(point: Point){
        val coordinates = Coordinates(((point.latitude * 1000000.0).roundToInt() /1000000.0).toString(),
            ((point.longitude * 1000000.0).roundToInt() /1000000.0).toString())
        newPost.value = newPost.value?.copy(coords = coordinates)
        inJob = false
    }

    fun addLink(link: String){
        if (link != ""){
            newPost.value = newPost.value?.copy(link = link)
        } else {
            newPost.value = newPost.value?.copy(link = null)
        }
    }

    fun addPictureToThePost(image: MultipartBody.Part){
        viewModelScope.launch {
            try {
                val attachment = repository.addPictureToThePost(AttachmentType.IMAGE, image)
                newPost.value = newPost.value?.copy(attachment = attachment)
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }


    fun deletePicture(){
        newPost.value = newPost.value?.copy(attachment = null)
    }


    fun deleteEditPost(){
        newPost.value = editedPost
        mentions.clear()
        mentionsLive.postValue(mentions)
    }

}