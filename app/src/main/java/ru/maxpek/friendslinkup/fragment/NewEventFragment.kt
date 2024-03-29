package ru.maxpek.friendslinkup.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
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
import ru.maxpek.friendslinkup.adapter.ListUsersIdEventAdapter
import ru.maxpek.friendslinkup.databinding.FragmentNewEventBinding
import ru.maxpek.friendslinkup.enumeration.TypeEvent
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.fragment.FeedFragment.Companion.intArg
import ru.maxpek.friendslinkup.fragment.MapsFragment.Companion.pointArg
import ru.maxpek.friendslinkup.util.GoDataTime
import ru.maxpek.friendslinkup.viewmodel.NewEventViewModel
import java.util.*


var dayEvent = 0
var monthEvent = 0
var yearEvent = 0
var hourEvent = 0
var minuteEvent = 0


@ExperimentalCoroutinesApi
@AndroidEntryPoint
@SuppressLint("FragmentBackPressedCallback")
class NewEventFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val newEventViewModel: NewEventViewModel by activityViewModels()

    @SuppressLint("UseRequireInsteadOfGet")
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


        var file: MultipartBody.Part

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Snackbar.make(binding.root, R.string.be_lost, Snackbar.LENGTH_SHORT)
                .setAction(R.string.exit) {
                    newEventViewModel.deleteEditPost()
                    findNavController().navigate(R.id.eventFragment)
                }.show()
        }

        if (arguments?.intArg != null) {
            val id = arguments?.intArg
            id?.let { newEventViewModel.getEvent(it) }
        }


        val adapter = ListUsersIdEventAdapter(object : AdapterUsersIdCallback {
            override fun goToPageUser(id: Int) {
                val idAuthor = id.toString()
                findNavController().navigate(
                    R.id.userJobFragment,
                    Bundle().apply { textArg = idAuthor })
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
                            "file", resultFile?.name, resultFile!!.asRequestBody()
                        )
                        newEventViewModel.addPictureToThePost(file)
                        binding.image.setImageURI(uri)
                    }
                }
            }


        binding.menuAdd.setOnClickListener {
            binding.menuAdd.isChecked = binding.image.isVisible
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
        }

        binding.mentionAdd.setOnClickListener {
            binding.mentionAdd.isChecked =
                newEventViewModel.newEvent.value?.speakerIds?.isNotEmpty() ?: false
            findNavController().navigate(R.id.action_newEventFragment_to_listOfUsersAddInEvent)
        }

        binding.geoAdd.setOnClickListener {
            if (newEventViewModel.newEvent.value?.coords != null) {
                binding.geoAdd.isChecked = true
                val point = Point(
                    newEventViewModel.newEvent.value?.coords!!.lat.toDouble(),
                    newEventViewModel.newEvent.value?.coords!!.long.toDouble()
                )
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

        binding.speakersIds.adapter = adapter
        newEventViewModel.speakersLive.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        newEventViewModel.newEvent.observe(viewLifecycleOwner) {
            it.content.let(binding.edit::setText)
            it.link.let(binding.editLink::setText)
            binding.mentionAdd.isChecked = it.speakerIds!!.isNotEmpty()
            binding.geoAdd.isChecked = newEventViewModel.newEvent.value?.coords != null
            binding.linkAdd.isChecked = newEventViewModel.newEvent.value?.link != null
            binding.dateTime.isChecked = newEventViewModel.newEvent.value?.datetime != null
            binding.type.isChecked = newEventViewModel.newEvent.value?.type == TypeEvent.ONLINE
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
            val date = newEventViewModel.newEvent.value?.datetime
            if (content == "" || date == null) {
                Snackbar.make(binding.root, R.string.content_field, Snackbar.LENGTH_SHORT).show()
            } else {
                newEventViewModel.addPost(content)
            }
        }
        binding.dateTime.setOnClickListener {
            binding.dateTime.isChecked = newEventViewModel.newEvent.value?.datetime != null
            getDataCalendar()
            DatePickerDialog(context!!, this, yearEvent, monthEvent, dayEvent).show()
        }
        binding.type.setOnClickListener {
            binding.type.isChecked = newEventViewModel.newEvent.value?.type == TypeEvent.ONLINE
            newEventViewModel.addTypeEvent()
        }

        newEventViewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        newEventViewModel.dataState.observe(viewLifecycleOwner) { state ->
            if (state.error) {
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_INDEFINITE)
                    .show()
            }
        }
        return binding.root
    }

    private fun getDataCalendar() {
        if (day == 0) {
            val cal = Calendar.getInstance()
            dayEvent = cal.get(Calendar.DAY_OF_MONTH)
            monthEvent = cal.get(Calendar.MONTH)
            yearEvent = cal.get(Calendar.YEAR)
        }
    }

    private fun getTimeCalendar() {
        if (day == 0) {
            val cal = Calendar.getInstance()
            hourEvent = cal.get(Calendar.HOUR)
            minuteEvent = cal.get(Calendar.MINUTE)
        }
    }


    override fun onDateSet(p0: DatePicker?, yearOf: Int, monthOf: Int, dayOfMonth: Int) {
        dayEvent = dayOfMonth
        monthEvent = monthOf
        yearEvent = yearOf
        getTimeCalendar()
        TimePickerDialog(context, this, hourEvent, minuteEvent, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        hourEvent = hourOfDay
        minuteEvent = minute
        val date = listOf(dayEvent, monthEvent, yearEvent, hourEvent, minuteEvent)
        val dateTime = GoDataTime.convertDateToLocalDate(date)
        newEventViewModel.addDateTime(dateTime)
    }
}