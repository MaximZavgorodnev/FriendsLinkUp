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

val emptyList = listOf<UserRequested>()
@HiltViewModel
class ListOfUserViewModel@Inject constructor(
    private val repositoryListOfUser : ListOfUserRepository,
    private val apiService: ApiService

): ViewModel() {
//    private val _data: MutableLiveData<List<UserRequested>>
//    get() = MutableLiveData(emptyList)

    var data: LiveData<List<UserRequested>>
    set() = LiveData(emptyList)

    fun getUsers() {
        viewModelScope.launch {
            try {
                try {
                    val response = apiService.getUsers()
                    if (!response.isSuccessful) {
                        throw ApiError(response.code(), response.message())
                    }
                    data.value = response.body() ?: throw ApiError(response.code(), response.message())

                } catch (e: IOException) {
                    throw NetworkError
                }
            } catch (e: Exception) {
//            _dataState.value = FeedModelState(error = true)
            }

        }
    }



}