package ru.maxpek.friendslinkup.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.posts.OnInteractionListener
import ru.maxpek.friendslinkup.adapter.posts.PagingLoadStateAdapter
import ru.maxpek.friendslinkup.adapter.posts.PostsAdapter
import ru.maxpek.friendslinkup.databinding.FragmentFeedBinding
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.util.IntArg
import ru.maxpek.friendslinkup.util.StringArg
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.NewPostViewModel
import ru.maxpek.friendslinkup.viewmodel.PostViewModel
import kotlin.system.exitProcess

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FeedFragment : Fragment() {
    private val viewModel: PostViewModel by activityViewModels()
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

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.exit)
        }

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (!post.likedByMe) viewModel.likeById(post.id) else viewModel.disLikeById(post.id)
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }
            override fun onEdit(post: PostResponse) {
                findNavController().navigate(R.id.action_feedFragment_to_newPostFragment,Bundle().apply { intArg = post.id })
            }
            override fun onRemove(post: PostResponse) {
                viewModel.removeById(post.id)
            }
            override fun onShare(post: PostResponse) {
                if (authViewModel.authenticated) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, post.content)
                        type = "text/plain"
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }
            override fun loadingTheListOfMentioned(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (post.mentionIds.isEmpty()){
                        Snackbar.make(binding.root, R.string.mention_anyone, Snackbar.LENGTH_SHORT).show()
                    } else {
                        viewModel.loadUsersMentions(post.mentionIds)
                        findNavController().navigate(R.id.action_feedFragment_to_listOfMentions)
                    }
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }

            override fun goToPageUser(post: PostResponse) {
                val idAuthor = post.authorId.toString()
                findNavController().navigate(R.id.userJobFragment,Bundle().apply { textArg = idAuthor })
            }

        })

        binding.list.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
                    adapter.retry()
                }
            }),
            footer = PagingLoadStateAdapter(object : PagingLoadStateAdapter.OnInteractionListener {
                override fun onRetry() {
                    adapter.retry()
                }
            }),
        )

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_loading) { viewModel.retry() }
                    .show()
            }
            if (state.loading){
                Snackbar.make(binding.root, R.string.problem_loading, Snackbar.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest(adapter::submitData)
        }

        adapter.loadStateFlow
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

    companion object {
        var Bundle.intArg: Int by IntArg
    }
}