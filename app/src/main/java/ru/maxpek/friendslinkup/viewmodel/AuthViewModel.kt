package ru.maxpek.friendslinkup.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.auth.AuthState
import ru.maxpek.friendslinkup.dto.PhotoModel
import ru.maxpek.friendslinkup.dto.UserRegistration
import ru.maxpek.friendslinkup.dto.UserResponse
import ru.maxpek.friendslinkup.repository.user.UserRepository
import ru.maxpek.friendslinkup.error.UnknownError
import java.io.File
import javax.inject.Inject

private val noPhoto = PhotoModel()

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appAuth: AppAuth,
    private val repositoryUser : UserRepository
) : ViewModel() {
    private val _photo = MutableLiveData(noPhoto)
    val photo: LiveData<PhotoModel>
        get() = _photo
    val data: LiveData<AuthState> = appAuth
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = appAuth.authStateFlow.value.id != 0L




    fun onSignIn(userResponse: UserResponse){
        viewModelScope.launch {
            try {
                repositoryUser.onSignIn(userResponse)

            } catch (e: Exception) {
                throw UnknownError
            }

        }
    }

    fun onSignUp(user: UserRegistration){
        viewModelScope.launch {
            try {
                repositoryUser.onSignUp(user)
            } catch (e: Exception) {
                throw UnknownError
            }

        }
    }

    fun changePhoto(uri: Uri?, file: File?) {
        _photo.value = PhotoModel(uri, file)
    }

}