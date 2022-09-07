package ru.maxpek.friendslinkup.fragment

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
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
import ru.maxpek.friendslinkup.databinding.FragmentNewPostBinding
import ru.maxpek.friendslinkup.dto.Coordinates
import ru.maxpek.friendslinkup.util.ArrayInt
import ru.maxpek.friendslinkup.util.PointArg
import ru.maxpek.friendslinkup.viewmodel.NewPostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewPostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )
        val newPostViewModel : NewPostViewModel by activityViewModels()
        var file: MultipartBody.Part

        val adapter = ListUsersIdAdapter(object : AdapterUsersIdCallback {
            override fun goToPageUser() {}
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
                        newPostViewModel.addPictureToThePost(file)
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
                            ImagePicker.with(this@NewPostFragment)
                                .crop(1F,1F)
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
            binding.mentionAdd.isChecked = newPostViewModel.newPost.value?.mentionIds?.isNotEmpty() ?: false
            findNavController().navigate(R.id.action_newPostFragment_to_listOfUsers)
        }

        binding.geoAdd.setOnClickListener {
//            findNavController().navigate(
//                R.id.action_newPostFragment_to_mapsFragment,
////                Bundle().apply { pointArg = Point(newPostViewModel.newPost.value?.coords.latitude.toS, marker.pointLongitude) })
//            )
            binding.geoAdd.isChecked = newPostViewModel.newPost.value?.coords != null
            findNavController().navigate(R.id.action_newPostFragment_to_mapsFragment)
        }

        binding.linkAdd.setOnClickListener {
            binding.linkAdd.isChecked = newPostViewModel.newPost.value?.link != null
            binding.editLink.visibility = View.VISIBLE
            binding.okAdd.visibility = View.VISIBLE
        }

        binding.okAdd.setOnClickListener {
            val link: String = binding.editLink.text.toString()
            newPostViewModel.addLink(link)
            binding.editLink.visibility = View.GONE
            binding.okAdd.visibility = View.GONE
        }

        binding.mentionIds.adapter = adapter
        newPostViewModel.newPost.observe(viewLifecycleOwner) {
            binding.mentionAdd.isChecked = it.mentionIds.isNotEmpty()
            adapter.submitList(newPostViewModel.mentionsLive.value)
            binding.geoAdd.isChecked = newPostViewModel.newPost.value?.coords != Coordinates("0", "0")
            binding.linkAdd.isChecked = newPostViewModel.newPost.value?.link != null
        }

        newPostViewModel.dataAttachment.observe(viewLifecycleOwner) {
            newPostViewModel.addAttachment()
            if (it.url != "") {
                binding.image.visibility = View.VISIBLE
                binding.menuAdd.isChecked = true
            } else {
                binding.menuAdd.isChecked = false
            }
        }

        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            newPostViewModel.addPost(content)

        }

        newPostViewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        ///Нужно доработать ошибку
        newPostViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry_loading) {
//                        newPostViewModel.retry()
                    }
                    .show()
            }
        }



        return binding.root
    }


    companion object {
        var Bundle.arrayInt: List<Int>? by ArrayInt
        var Bundle.pointArg: Point by PointArg
    }
}