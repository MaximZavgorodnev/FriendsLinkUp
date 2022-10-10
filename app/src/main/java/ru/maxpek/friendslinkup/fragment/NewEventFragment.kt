package ru.maxpek.friendslinkup.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.AdapterUsersIdCallback
import ru.maxpek.friendslinkup.adapter.ListUsersIdAdapter
import ru.maxpek.friendslinkup.databinding.FragmentNewEventBinding
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.fragment.FeedFragment.Companion.intArg
import ru.maxpek.friendslinkup.fragment.MapsFragment.Companion.pointArg
import ru.maxpek.friendslinkup.viewmodel.NewEventViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@SuppressLint("FragmentBackPressedCallback")
class NewEventFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewEventBinding.inflate(
            inflater,
            container,
            false
        )

        val newEventViewModel : NewEventViewModel by activityViewModels()
        var file: MultipartBody.Part

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Snackbar.make(binding.root, R.string.be_lost, Snackbar.LENGTH_SHORT).setAction(R.string.exit) {
                newEventViewModel.deleteEditPost()
                findNavController().navigate(R.id.eventFragment)
            }.show()
        }

        if (arguments?.intArg != null) {
            val id = arguments?.intArg
            id?.let {newEventViewModel.getEvent(it) }
        }




        val adapter = ListUsersIdAdapter(object : AdapterUsersIdCallback {
            override fun goToPageUser(id: Int) {
                val idAuthor = id.toString()
                findNavController().navigate(R.id.userJobFragment,Bundle().apply { textArg = idAuthor })
            }
        })

        val pickPhotoLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                when (it.resultCode) {
                    ImagePicker.RESULT_ERROR -> {
                        Snackbar.make(
                            binding.root,
                            ImagePicker.getError(it.data),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                    Activity.RESULT_OK -> {
                        val uri: Uri? = it.data?.data
                        val resultFile = uri?.toFile()
                        file = MultipartBody.Part.createFormData(
                            "file", resultFile?.name, resultFile!!.asRequestBody())
                        newEventViewModel.addPictureToThePost(file)
                        binding.image.setImageURI(uri)
                    }
                }
            }


        binding.menuAdd.setOnClickListener {
            binding.menuAdd.isChecked = binding.image.isVisible
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_add_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.audio -> {

                            true
                        }
                        R.id.video -> {

                            true
                        }
                        R.id.image -> {
                            ImagePicker.with(this@NewEventFragment)
                                .crop()
                                .compress(2048)
                                .provider(ImageProvider.BOTH)
                                .galleryMimeTypes(
                                    arrayOf(
                                        "image/png",
                                        "image/jpeg",
                                    )
                                )
                                .createIntent(pickPhotoLauncher::launch)
                            true
                        }
                        else -> false
                    }
                }
            }.show()
        }

        binding.mentionAdd.setOnClickListener {
            binding.mentionAdd.isChecked = newEventViewModel.newEvent.value?.speakerIds?.isNotEmpty() ?: false
            findNavController().navigate(R.id.action_newEventFragment_to_listOfUsersAddInEvent)
        }

        binding.geoAdd.setOnClickListener {
            if (newEventViewModel.newEvent.value?.coords != null) {
                binding.geoAdd.isChecked = true
                val point = Point(newEventViewModel.newEvent.value?.coords!!.lat.toDouble(),
                    newEventViewModel.newEvent.value?.coords!!.long.toDouble() )
                newEventViewModel.inJob = true
                findNavController().navigate(
                    R.id.action_newEventFragment_to_mapsFragment,
                    Bundle().apply { pointArg = point })
            } else {
                newEventViewModel.inJob = true
                findNavController().navigate(R.id.action_newEventFragment_to_mapsFragment)
            }
        }

        binding.linkAdd.setOnClickListener {
            binding.linkAdd.isChecked = newEventViewModel.newEvent.value?.link != null
            binding.editLink.visibility = View.VISIBLE
            binding.okAdd.visibility = View.VISIBLE
        }

        binding.okAdd.setOnClickListener {
            val link: String = binding.editLink.text.toString()
            newEventViewModel.addLink(link)
            binding.editLink.visibility = View.GONE
            binding.okAdd.visibility = View.GONE
        }

        binding.mentionIds.adapter = adapter
        newEventViewModel.speakersLive.observe(viewLifecycleOwner) {
            val user = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (user) {
                    binding.mentionIds.smoothScrollToPosition(0)
                }
            }

        }
        newEventViewModel.newEvent.observe(viewLifecycleOwner) {
            it.content.let(binding.edit::setText)
            it.link.let(binding.editLink::setText)
            binding.mentionAdd.isChecked = it.speakerIds!!.isNotEmpty()
            binding.geoAdd.isChecked = newEventViewModel.newEvent.value?.coords != null
            binding.linkAdd.isChecked = newEventViewModel.newEvent.value?.link != null
            if (it.attachment != null) {
                binding.image.visibility = View.VISIBLE
                Glide.with(this)
                    .load(it.attachment.url)
                    .error(R.drawable.ic_avatar_loading_error_48)
                    .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                    .timeout(10_000)
                    .into(binding.image)
                binding.menuAdd.isChecked = true
            } else {
                binding.menuAdd.isChecked = false
                binding.image.visibility = View.GONE
            }

        }

        binding.image.setOnClickListener {
            newEventViewModel.deletePicture()
        }

        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (content =="") {
                Snackbar.make(binding.root, R.string.content_field, Snackbar.LENGTH_SHORT).show()
            } else {
                newEventViewModel.addPost(content)
            }
        }

        newEventViewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        newEventViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE).show()
            }
        }


        return binding.root
    }
}