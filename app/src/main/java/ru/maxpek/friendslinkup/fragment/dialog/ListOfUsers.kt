package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import ru.maxpek.friendslinkup.databinding.FaragmenListOfUsersBinding

class ListOfUsers: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }
}