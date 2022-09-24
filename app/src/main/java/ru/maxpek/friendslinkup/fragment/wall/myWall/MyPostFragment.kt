package ru.maxpek.friendslinkup.fragment.wall.myWall

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.myWall.posts.MyPostsAdapter
import ru.maxpek.friendslinkup.adapter.myWall.posts.MyWallOnInteractionListener
import ru.maxpek.friendslinkup.adapter.posts.PagingLoadStateAdapter
import ru.maxpek.friendslinkup.databinding.FragmentMyWallPostBinding
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.MyWallPostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyPostFragment: Fragment() {
    private val viewModel: MyWallPostViewModel by activityViewModels()
    private val authViewModel: AuthViewModel by viewModels()
    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyWallPostBinding.inflate(inflater, container, false)

        val adapter = MyPostsAdapter(object : MyWallOnInteractionListener {
            override fun onLike(post: PostResponse) {
                if (authViewModel.authenticated) {
                    if (!post.likedByMe) viewModel.likeById(post.id) else viewModel.disLikeById(post.id)
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_feedFragment_to_authenticationFragment)
                }
            }
            override fun onEdit(post: PostResponse) {}
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

        lifecycleScope.launchWhenCreated {
            viewModel.data.collectLatest(adapter::submitData)
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest {
                binding.swiperefresh.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {

                inflate(R.menu.menu_navigation)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.posts -> {
                            Snackbar.make(binding.root, R.string.you_are_where, Snackbar.LENGTH_SHORT).show()
                            true
                        }
                        R.id.events -> {
                            findNavController().navigate(R.id.action_myPostFragment_to_myEventFragment2)
                            true
                        }

                        R.id.jobs -> {
                            findNavController().navigate(R.id.action_myPostFragment_to_myJobFragment2)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        binding.home.setOnClickListener {
            viewModel.removeAll()
            findNavController().setGraph(R.id.myPostFragment)
//            if (findNavController().currentDestination?.id == R.id.feedFragment) {
//                findNavController().navigate(R.id.action_myPostFragment_to_feedFragment)
//            }
            findNavController().navigate(R.id.action_myPostFragment_to_feedFragment)
        }

        return binding.root
    }
}