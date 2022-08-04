package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.auth.AuthState
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.error.ApiError
import ru.maxpek.friendslinkup.error.NetworkError
import ru.maxpek.friendslinkup.model.FeedModelState
import ru.maxpek.friendslinkup.repository.listOfUsers.ListOfUserRepository
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ListOfUserViewModel@Inject constructor(
    private val repositoryListOfUser : ListOfUserRepository): ViewModel() {

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