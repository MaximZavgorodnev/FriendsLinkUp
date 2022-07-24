package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.auth.AuthState
import ru.maxpek.friendslinkup.dto.User
import ru.maxpek.friendslinkup.repository.user.UserRepository
import ru.maxpek.friendslinkup.error.UnknownError
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(
    private  val appAuth: AppAuth,
    private val repositoryUser : UserRepository
) : ViewModel() {
    val data: LiveData<AuthState> = appAuth
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = appAuth.authStateFlow.value.id != 0L




    fun onSignIn(user: User){
        viewModelScope.launch {
            try {
                repositoryUser.onSignIn(user)

            } catch (e: Exception) {
                throw UnknownError
            }

        }
    }

    fun onSignUp(loginEditText: String, passwordEditText: String, usernameEditText: String){
        viewModelScope.launch {
            try {
                repositoryUser.onSignUp(loginEditText, passwordEditText, usernameEditText)
            } catch (e: Exception) {
                throw UnknownError
            }

        }
    }

}