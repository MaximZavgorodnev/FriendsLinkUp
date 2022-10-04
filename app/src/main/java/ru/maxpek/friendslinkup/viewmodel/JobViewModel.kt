package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.repository.myWall.job.MyWallJobRepository
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class JobViewModel@Inject constructor(
    private val repository: MyWallJobRepository,
    appAuth: AppAuth
): ViewModel() {
}