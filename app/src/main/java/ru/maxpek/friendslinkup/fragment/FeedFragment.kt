package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentFeedBinding
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.NewPostViewModel
import ru.maxpek.friendslinkup.viewmodel.PostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)
    val newPostViewModel : NewPostViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)
        authViewModel.data.observeForever {
            if (!authViewModel.authenticated) {binding.fab.visibility = View.GONE} else {
                binding.fab.visibility = View.VISIBLE
            }
        }
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }
        return binding.root
    }
}