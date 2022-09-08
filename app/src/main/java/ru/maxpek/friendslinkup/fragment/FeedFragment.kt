package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.posts.OnInteractionListener
import ru.maxpek.friendslinkup.adapter.posts.PagingLoadStateAdapter
import ru.maxpek.friendslinkup.adapter.posts.PostsAdapter
import ru.maxpek.friendslinkup.databinding.FragmentFeedBinding
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.NewPostViewModel
import ru.maxpek.friendslinkup.viewmodel.PostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
//    val newPostViewModel : NewPostViewModel by viewModels()
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

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: PostResponse) {}
            override fun onEdit(post: PostResponse) {}
            override fun onRemove(post: PostResponse) {}
            override fun onShare(post: PostResponse) {}
            override fun loadingTheListOfMentioned(mentionIds: List<Int>) {}

        })

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
//                    adapter.retry()
                }
            }),
            footer = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
//                    adapter.retry()
                }
            }),
        )

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest(adapter::submitData)
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swiperefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        binding.swiperefresh.setOnRefreshListener {
            adapter.refresh()
        }

        return binding.root
    }
}