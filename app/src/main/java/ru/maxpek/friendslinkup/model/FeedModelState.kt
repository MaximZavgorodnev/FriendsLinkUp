package ru.maxpek.friendslinkup.model

data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val systemError: Boolean = false
)
