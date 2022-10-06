package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.dto.Job
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.myWall.job.MyWallJobRepository
import ru.maxpek.friendslinkup.util.SingleLiveEvent
import javax.inject.Inject

val jobZero = Job(
    id = 0,
    name = "",
    position = "",
    start = "",
    finish = "",
    link = ""
)
@ExperimentalCoroutinesApi
@HiltViewModel
class JobViewModel@Inject constructor(
    private val repository: MyWallJobRepository
): ViewModel() {
    val data: MutableLiveData<List<Job>> = MutableLiveData(repository.dateJob)

    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    val dateStart = MutableLiveData<String>()
    val dateFinish = MutableLiveData<String>()

    private val _editedJob: MutableLiveData<Job> = MutableLiveData(jobZero)
    val editedJob: LiveData<Job>
    get() = _editedJob

    fun getMyJob(){
        viewModelScope.launch {
            try {
                repository.getMyJob()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun removeById(id: Int) {
        viewModelScope.launch {
            try {
                repository.removeById(id)
                _dataState.value = FeedModelState()
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun editJob(job: Job){
        _editedJob.value = job
    }

    fun addJob(){
        viewModelScope.launch {
            try {
                val job = _editedJob.value
                job?.let { repository.save(it) }
                _postCreated.value = Unit
                _dataState.value = FeedModelState(error = false)
                _editedJob.postValue(jobZero)
            } catch (e: RuntimeException) {
                _dataState.value = FeedModelState(error = true)
            }
        }
    }

    fun addDateStart(date: String) {
        dateStart.postValue(date)
    }

    fun addDateFinish(date: String) {
        dateFinish.postValue(date)
    }

    fun deleteEditJob(){
        _editedJob.postValue(jobZero)
        dateFinish.value = ""
        dateStart.value = ""
    }
}