package ru.maxpek.friendslinkup.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.maxpek.friendslinkup.repository.listOfUsers.ListOfUserRepository
import javax.inject.Inject

@HiltViewModel
class ListOfUserViewModel@Inject constructor(
    private val repositoryListOfUser : ListOfUserRepository,

): ViewModel() {
}