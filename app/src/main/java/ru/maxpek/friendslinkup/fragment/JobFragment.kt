package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.databinding.FragmentJobBinding
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class JobFragment: Fragment() {
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentJobBinding.inflate(inflater, container, false)
        authViewModel.data.observeForever {
            if (!authViewModel.authenticated) {binding.fab.visibility = View.GONE} else {
                binding.fab.visibility = View.VISIBLE
            }
        }
        return binding.root
    }
}