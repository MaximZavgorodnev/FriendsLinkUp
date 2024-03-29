package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.AdapterUsersIdCallback
import ru.maxpek.friendslinkup.adapter.UsersListAdapter
import ru.maxpek.friendslinkup.databinding.FaragmenListOfUsersBinding
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.fragment.FeedFragment.Companion.intArg
import ru.maxpek.friendslinkup.viewmodel.MyWallPostViewModel
import ru.maxpek.friendslinkup.viewmodel.PostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@OptIn(ExperimentalCoroutinesApi::class)
class ListOfMentions : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(inflater, container, false)

        val postViewModel: PostViewModel by activityViewModels()
        val viewModel: MyWallPostViewModel by activityViewModels()



        val adapter = UsersListAdapter(object : AdapterUsersIdCallback {
            override fun goToPageUser(id: Int) {
                val idAuthor = id.toString()
                findNavController().navigate(
                    R.id.userJobFragment,
                    Bundle().apply { textArg = idAuthor })
            }
        })
        binding.list.adapter = adapter

        if (arguments?.intArg == 1) {
            viewModel.dataState.observe(viewLifecycleOwner) { state ->
                if (state.loading) {
                    Snackbar.make(binding.root, R.string.problem_loading, Snackbar.LENGTH_SHORT).show()
                }
            }

            viewModel.dataUserMentions.observe(viewLifecycleOwner) {
                val newUser = adapter.itemCount < it.size
                adapter.submitList(it) {
                    if (newUser) {
                        binding.list.smoothScrollToPosition(0)
                    }
                }
            }
        } else {
            postViewModel.dataState.observe(viewLifecycleOwner) { state ->
                if (state.loading) {
                    Snackbar.make(binding.root, R.string.problem_loading, Snackbar.LENGTH_SHORT).show()
                }
            }

            postViewModel.dataUserMentions.observe(viewLifecycleOwner) {
                val newUser = adapter.itemCount < it.size
                adapter.submitList(it) {
                    if (newUser) {
                        binding.list.smoothScrollToPosition(0)
                    }
                }
            }
        }


        binding.enter.visibility = View.GONE
        return binding.root
    }

}