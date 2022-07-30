package ru.maxpek.friendslinkup.auth

import android.content.SharedPreferences
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import ru.maxpek.friendslinkup.api.ApiService
import ru.maxpek.friendslinkup.dto.PushToken
import ru.maxpek.friendslinkup.error.ApiError
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppAuth @Inject constructor(
    private val prefs: SharedPreferences,
    private val apiService: ApiService
) {
    companion object{
        const val idKey = "id"
        const val tokenKey = "token"
        const val avatar = "avatarUser"
        const val name = "nameUser"
    }


    private val _authStateFlow: MutableStateFlow<AuthState>

    init {
        val id = prefs.getLong(idKey, 0)
        val token = prefs.getString(tokenKey, null)

        if (id == 0L || token == null) {
            _authStateFlow = MutableStateFlow(AuthState())
            with(prefs.edit()) {
                clear()
                apply()
            }
        } else {
            _authStateFlow = MutableStateFlow(AuthState(id, token))
        }
    }

    val authStateFlow: StateFlow<AuthState> = _authStateFlow.asStateFlow()

    @Synchronized
    fun setAuth(id: Long, token: String, avatarUser: String?, nameUser: String?) {
        _authStateFlow.value = AuthState(id, token, avatarUser)
        with(prefs.edit()) {
            putLong(idKey, id)
            putString(tokenKey, token)
            putString(avatar, avatarUser)
            putString(name, nameUser)
            apply()
        }
        sendPushToken()
    }

    @Synchronized
    fun removeAuth() {
        _authStateFlow.value = AuthState()
        with(prefs.edit()) {
            clear()
            commit()
        }
        sendPushToken()
    }

    fun sendPushToken(token: String? = null) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val pushToken = PushToken(token ?: Firebase.messaging.token.await())
                apiService.save(pushToken)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

data class AuthState(val id: Long = 0, val token: String? = null,
                     val avatarUser: String? = null, val nameUser: String? = null)
