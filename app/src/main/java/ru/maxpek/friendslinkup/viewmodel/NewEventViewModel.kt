package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.dto.EventCreateRequest
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.newEvent.NewEventRepository
import ru.maxpek.friendslinkup.util.SingleLiveEvent
import javax.inject.Inject
import kotlin.math.roundToInt

val editedEvent = EventCreateRequest(
    id = 0,
    content = "",
    datetime = null,
    coords = null,
    type = null,
    attachment = null,
    link = null,
    speakerIds = listOf())
@ExperimentalCoroutinesApi
@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val repository: NewEventRepository): ViewModel() {

    var inJob = false

    val newEvent: MutableLiveData<EventCreateRequest> = MutableLiveData(editedEvent)

    val data: MutableLiveData<List<UserRequested>> = MutableLiveData()

    val speakersLive : MutableLiveData<MutableList<UserRequested>> = MutableLiveData()

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    fun getEvent(id: Int){

        speakersLive.postValue(mentions)
        viewModelScope.launch {
            try {
                newEvent.value = repository.getEvent(id)
                newEvent.value?.speakerIds?.forEach {
                    speakersLive.value!!.add(repository.getUser(it))
                }
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun getUsers() {
        speakersLive.postValue(mentions)
        viewModelScope.launch {
            try {
                data.value = repository.loadUsers()
                data.value?.forEach { user ->
                    newEvent.value?.speakerIds?.forEach {
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

    fun addSpeakersIds(){
        speakersLive.postValue(mentions)
        val listChecked = mutableListOf<Int>()
        val speakersUserList = mutableListOf<UserRequested>()
        data.value?.forEach { user ->
            if (user.checked){
                listChecked.add(user.id)
                speakersUserList.add(user)
            }
        }
        speakersLive.value = speakersUserList
        newEvent.value = newEvent.value?.copy(speakerIds = listChecked)
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
        newEvent.value = newEvent.value?.copy(content = content)
        val event = newEvent.value!!
        _postCreated.value = Unit
        viewModelScope.launch {
            try {
                repository.addEvent(event)
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addCoords(point: Point){
        val coordinates = Coordinates(((point.latitude * 1000000.0).roundToInt() /1000000.0).toString(),
            ((point.longitude * 1000000.0).roundToInt() /1000000.0).toString())
        newEvent.value = newEvent.value?.copy(coords = coordinates)
        inJob = false
    }

    fun addLink(link: String){
        if (link != ""){
            newEvent.value = newEvent.value?.copy(link = link)
        } else {
            newEvent.value = newEvent.value?.copy(link = null)
        }
    }

    fun addPictureToThePost(image: MultipartBody.Part){
        viewModelScope.launch {
            try {
                val attachment = repository.addPictureToTheEvent(AttachmentType.IMAGE, image)
                newEvent.value = newEvent.value?.copy(attachment = attachment)
                _dataState.value = FeedModelState(error = false)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun deletePicture(){
        newEvent.value = newEvent.value?.copy(attachment = null)
    }


    fun deleteEditPost(){
        newEvent.value = editedEvent
        mentions.clear()
        speakersLive.value = mentions
    }
}