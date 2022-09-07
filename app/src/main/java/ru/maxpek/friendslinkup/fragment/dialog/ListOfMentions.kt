@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.adapter.AdapterUsersIdCallback
import ru.maxpek.friendslinkup.adapter.ListOfUsersChoiceAdapter
import ru.maxpek.friendslinkup.adapter.UsersListAdapter
import ru.maxpek.friendslinkup.databinding.FaragmenListOfUsersBinding
import ru.maxpek.friendslinkup.viewmodel.PostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListOfMentions : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(inflater, container, false)

        val postViewModel: PostViewModel by activityViewModels()


        val adapter = UsersListAdapter(object : AdapterUsersIdCallback {
            override fun goToPageUser() {}
        })
        binding.list.adapter = adapter

        postViewModel.dataUserMentions.observe(viewLifecycleOwner) {
            val newUser = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newUser) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        return binding.root
    }

}