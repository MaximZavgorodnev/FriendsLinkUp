package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentEventBinding
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.PostViewModel


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EventFragment: Fragment() {

    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventBinding.inflate(inflater, container, false)
        authViewModel.data.observeForever {
            if (!authViewModel.authenticated) {binding.fab.visibility = View.GONE} else {
                binding.fab.visibility = View.VISIBLE
            }
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_eventFragment_to_newEventFragment)
        }
        return binding.root
    }
}


