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
import ru.maxpek.friendslinkup.util.SingleLiveEvent
import java.text.DecimalFormat
import javax.inject.Inject
import kotlin.math.roundToInt


val editedPost = PostCreateRequest(
    id = 0,
    content = "",
    coords = Coordinates("0", "0"),
    link = null,
    attachment = null,
    mentionIds = listOf())

val mentions = mutableListOf<UserRequested>()
@ExperimentalCoroutinesApi
@HiltViewModel
class NewPostViewModel @Inject constructor(
    private val repository : NewPostRepository): ViewModel() {

    val newPost: MutableLiveData<PostCreateRequest> = MutableLiveData(editedPost)

    val data: MutableLiveData<List<UserRequested>> = MutableLiveData()

    val mentionsLive : MutableLiveData<MutableList<UserRequested>> = MutableLiveData()
    val dataAttachment = repository.dataAttachment


    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun getPost(id: Int){
        mentionsLive.value = mentions
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
        mentionsLive.value = mentions
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
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addMentionIds(){
        mentionsLive.value = mentions
        val listChecked = mutableListOf<Int>()
        val mentionsUserList = mutableListOf<UserRequested>()
        data.value?.forEach { user ->
            if (user.checked){
                listChecked.add(user.id)
                mentionsUserList.add(user)
            }
        }
        mentionsLive.value = mentionsUserList
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
        _postCreated.value = Unit
        viewModelScope.launch {
            try {
                repository.addPost(post)
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addCoords(point: Point){
        val coordinates = Coordinates(((point.latitude * 1000000.0).roundToInt() /1000000.0).toString(),
            ((point.longitude * 1000000.0).roundToInt() /1000000.0).toString())
        newPost.value = newPost.value?.copy(coords = coordinates)
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
                repository.addPictureToThePost(AttachmentType.IMAGE, image)
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addAttachment(){
        newPost.value = newPost.value?.copy(attachment = dataAttachment.value)
    }





    fun deleteEditPost(){
        newPost.value = editedPost
        mentions.clear()
        mentionsLive.value = mentions
    }

}