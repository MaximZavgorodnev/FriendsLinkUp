package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
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
import ru.maxpek.friendslinkup.adapter.events.EventCallback
import ru.maxpek.friendslinkup.adapter.events.EventsAdapter
import ru.maxpek.friendslinkup.adapter.posts.PagingLoadStateAdapter
import ru.maxpek.friendslinkup.databinding.FragmentEventBinding
import ru.maxpek.friendslinkup.dto.EventResponse
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.util.IntArg
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.EventViewModel


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EventFragment : Fragment() {

    private val authViewModel: AuthViewModel by viewModels()
    private val viewModel: EventViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentEventBinding.inflate(inflater, container, false)
        authViewModel.data.observeForever {
            if (!authViewModel.authenticated) {
                binding.fab.visibility = View.GONE
            } else {
                binding.fab.visibility = View.VISIBLE
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigate(R.id.exit)
        }

        val adapter = EventsAdapter(object : EventCallback {
            override fun onLike(event: EventResponse) {
                if (authViewModel.authenticated) {
                    if (!event.likedByMe) viewModel.likeById(event.id) else viewModel.disLikeById(
                        event.id
                    )
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authenticationFragment)
                }
            }

            override fun onEdit(event: EventResponse) {
                findNavController().navigate(
                    R.id.action_eventFragment_to_newEventFragment,
                    Bundle().apply { intArg = event.id })
            }

            override fun onRemove(event: EventResponse) {
                viewModel.removeById(event.id)
            }

            override fun loadingTheListOfSpeakers(event: EventResponse) {
                if (authViewModel.authenticated) {
                    if (event.speakerIds.isEmpty()) {
                        Snackbar.make(binding.root, R.string.mention_anyone, Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.loadUsersSpeakers(event.speakerIds)
                        findNavController().navigate(R.id.action_eventFragment_to_listOfSpeakers)
                    }
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authenticationFragment)
                }
            }

            override fun goToPageUser(event: EventResponse) {
                val idAuthor = event.authorId.toString()
                findNavController().navigate(
                    R.id.userJobFragment,
                    Bundle().apply { textArg = idAuthor })
            }

            override fun onParticipateInEvent(event: EventResponse) {
                if (authViewModel.authenticated) {
                    if (!event.participatedByMe) viewModel.participateInEvent(event.id) else viewModel.doNotParticipateInEvent(
                        event.id
                    )
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authenticationFragment)
                }
            }

            override fun loadingTheListOfParticipants(event: EventResponse) {
                if (authViewModel.authenticated) {
                    if (event.participantsIds.isEmpty()) {
                        Snackbar.make(binding.root, R.string.mention_anyone, Snackbar.LENGTH_SHORT)
                            .show()
                    } else {
                        viewModel.goToUserParticipateInEvent(event.participantsIds)
                        findNavController().navigate(R.id.action_eventFragment_to_listOfSpeakers)
                    }
                } else {
                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.authenticationFragment)
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

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_loading) { viewModel.retry() }
                    .show()
            }
            if (state.loading) {
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
            findNavController().navigate(R.id.action_eventFragment_to_newEventFragment)
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


