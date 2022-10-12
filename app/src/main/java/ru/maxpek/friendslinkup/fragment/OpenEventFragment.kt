package ru.maxpek.friendslinkup.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentOpenEventBinding
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.enumeration.TypeEvent
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.fragment.EventFragment.Companion.intArg
import ru.maxpek.friendslinkup.fragment.MapsFragment.Companion.pointArg
import ru.maxpek.friendslinkup.util.GoDataTime
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.EventViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class OpenEventFragment : Fragment() {
    private val authViewModel: AuthViewModel by activityViewModels()
    private val viewModel: EventViewModel by activityViewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentOpenEventBinding.inflate(
            inflater,
            container,
            false
        )
        if (arguments?.intArg != null) {
            val id = arguments?.intArg
            id?.let { viewModel.getEvent(it) }
        } else {
            viewModel.getEvent(0)
        }
        binding.allEvent.visibility = View.GONE

        viewModel.dataState.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.refreshing

            if (state.loading) {
                Snackbar.make(binding.root, R.string.problem_loading, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.eventResponse.observe(viewLifecycleOwner) { event ->
            if (event.id == arguments?.intArg) {
                binding.allEvent.visibility = View.VISIBLE
                binding.eventContent.apply {
                    video.visibility = View.GONE
                    backgroundVideo.visibility = View.GONE

                    Glide.with(this@OpenEventFragment)
                        .load(event.authorAvatar)
                        .error(R.drawable.ic_avatar_loading_error_48)
                        .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                        .timeout(10_000)
                        .circleCrop()
                        .into(avatar)


                    if (event.attachment != null) {
                        when (event.attachment.type) {
                            AttachmentType.IMAGE -> {
                                backgroundVideo.visibility = View.VISIBLE
                            }
                            AttachmentType.VIDEO -> {
                                video.visibility = View.VISIBLE
                                backgroundVideo.visibility = View.VISIBLE
                            }
                            AttachmentType.AUDIO -> {}
                        }
                        Glide.with(this@OpenEventFragment).load(event.attachment.url)
                            .timeout(10_000).into(backgroundVideo)
                    }

                    when (event.type) {
                        TypeEvent.OFFLINE -> type.setImageResource(R.drawable.ic_sensors_off_32)
                        TypeEvent.ONLINE -> type.setImageResource(R.drawable.ic_sensors_32)
                    }
                    author.text = event.author
                    published.text = GoDataTime.convertDataTime(event.published)
                    dateTime.text = GoDataTime.convertDataTime(event.datetime)
                    content.text = event.content + if (event.link != null) {
                        "\n" + event.link
                    } else {
                        ""
                    }
                    like.isChecked = event.likedByMe
                    like.text = "${event.likeOwnerIds.size}"
                    participate.isChecked = event.participatedByMe
                    participate.text = "${event.participantsIds.size}"
                    geo.visibility = if (event.coords != null) View.VISIBLE else View.INVISIBLE
                    menu.visibility = if (event.ownerByMe) View.VISIBLE else View.INVISIBLE
                    speakers.text = "${event.speakerIds.size}"

                    menu.setOnClickListener {
                        PopupMenu(it.context, it).apply {
                            inflate(R.menu.options_post)
                            setOnMenuItemClickListener { item ->
                                when (item.itemId) {
                                    R.id.remove -> {
                                        viewModel.removeById(event.id)
                                        true
                                    }
                                    R.id.edit -> {
                                        findNavController().navigate(R.id.newEventFragment,
                                            Bundle().apply { intArg = event.id })
                                        true
                                    }

                                    else -> false
                                }
                            }
                        }.show()
                    }

                    like.setOnClickListener {
                        if (authViewModel.authenticated) {
                            if (!event.likedByMe) viewModel.likeById(event.id) else viewModel.disLikeById(
                                event.id
                            )
                        } else {
                            Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.authenticationFragment)
                        }
                    }
                    participate.setOnClickListener {
                        if (authViewModel.authenticated) {
                            if (!event.participatedByMe) viewModel.participateInEvent(event.id) else viewModel.doNotParticipateInEvent(
                                event.id
                            )
                        } else {
                            Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.authenticationFragment)
                        }
                    }
                    participate.setOnLongClickListener {
                        if (authViewModel.authenticated) {
                            if (event.participantsIds.isEmpty()) {
                                Snackbar.make(
                                    binding.root,
                                    R.string.mention_anyone,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.goToUserParticipateInEvent(event.participantsIds)
                                findNavController().navigate(R.id.listOfSpeakers)
                            }
                        } else {
                            Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.authenticationFragment)
                        }
                        true
                    }

                    backgroundVideo.setOnClickListener {
                        it.findNavController().navigate(
                            R.id.displayingImagesFragment2,
                            Bundle().apply { textArg = event.attachment?.url ?: " " })
                    }
                    speakers.setOnClickListener {
                        if (authViewModel.authenticated) {
                            if (event.speakerIds.isEmpty()) {
                                Snackbar.make(
                                    binding.root,
                                    R.string.mention_anyone,
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.loadUsersSpeakers(event.speakerIds)
                                findNavController().navigate(R.id.listOfSpeakers)
                            }
                        } else {
                            Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.authenticationFragment)
                        }
                    }

                    author.setOnClickListener {
                        val idAuthor = event.authorId.toString()
                        findNavController().navigate(
                            R.id.userJobFragment,
                            Bundle().apply { textArg = idAuthor })
                    }

                    avatar.setOnClickListener {
                        val idAuthor = event.authorId.toString()
                        findNavController().navigate(
                            R.id.userJobFragment,
                            Bundle().apply { textArg = idAuthor })
                    }

                    geo.setOnClickListener {
                        it.findNavController().navigate(
                            R.id.mapsFragment,
                            Bundle().apply {
                                Point(
                                    event.coords?.lat!!.toDouble(), event.coords.long.toDouble()
                                ).also { pointArg = it }
                            })
                    }

                }
            }
        }

        return binding.root
    }
}