package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.adapter.AdapterCallback
import ru.maxpek.friendslinkup.adapter.ListOfUsersAdapter
import ru.maxpek.friendslinkup.databinding.FaragmenListOfUsersBinding
import ru.maxpek.friendslinkup.fragment.NewPostFragment.Companion.arrayInt
import ru.maxpek.friendslinkup.viewmodel.ListOfUserViewModel
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ListOfUsers: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(inflater, container, false)
        val viewModel: ListOfUserViewModel by viewModels()
        val usersListChecked: List<Int>?
        if (arguments != null){
            usersListChecked = arguments?.arrayInt

        }
        viewModel.getUsers()
        val adapter = ListOfUsersAdapter(object : AdapterCallback {
            override fun onChecked(id: Long) {}
        })
        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
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