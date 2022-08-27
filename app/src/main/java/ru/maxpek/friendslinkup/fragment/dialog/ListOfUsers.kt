package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.adapter.AdapterCallback
import ru.maxpek.friendslinkup.adapter.ListOfUsersAdapter
import ru.maxpek.friendslinkup.databinding.FaragmenListOfUsersBinding
import ru.maxpek.friendslinkup.dto.UserRequested
import ru.maxpek.friendslinkup.fragment.NewPostFragment.Companion.arrayInt
import ru.maxpek.friendslinkup.viewmodel.NewPostViewModel



@AndroidEntryPoint
class ListOfUsers: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(inflater, container, false)

        val newPostViewModel: NewPostViewModel by viewModels()

        if (newPostViewModel.data.value!!.isEmpty()) {
            newPostViewModel.getUsers()
        }

        val adapter = ListOfUsersAdapter(object : AdapterCallback {
            override fun isChecked(id: Int) {
                newPostViewModel.isChecked(id)
            }
            override fun unChecked(id: Int) {
                newPostViewModel.unChecked(id)
            }
        })
        binding.list.adapter = adapter

        newPostViewModel.data.observe(viewLifecycleOwner) {
            val newUser = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newUser) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.enter.setOnClickListener {
            newPostViewModel.addMentionIds()
            findNavController().navigateUp()
        }
        return binding.root
    }


}