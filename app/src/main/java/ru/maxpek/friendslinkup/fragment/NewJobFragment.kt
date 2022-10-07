package ru.maxpek.friendslinkup.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentNewJobBinding
import ru.maxpek.friendslinkup.dto.Job
import ru.maxpek.friendslinkup.util.AndroidUtils
import ru.maxpek.friendslinkup.util.GoDataTime
import ru.maxpek.friendslinkup.viewmodel.JobViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*


var day = 0
var month = 0
var year = 0
var startEndFinished = true

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewJobFragment: Fragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: JobViewModel by activityViewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("FragmentBackPressedCallback", "UseRequireInsteadOfGet",
        "ClickableViewAccessibility"
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewJobBinding.inflate(
            inflater,
            container,
            false
        )

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            Snackbar.make(binding.root, R.string.be_lost, Snackbar.LENGTH_SHORT).setAction(R.string.exit) {
                viewModel.deleteEditJob()
                findNavController().navigateUp()
            }.show()
        }

        viewModel.editedJob.observe(viewLifecycleOwner) {
            val start = GoDataTime.convertDataTimeJob(it.start)
            val end = it.finish?.let { it1 -> GoDataTime.convertDataTimeJob(it1) }
            if (binding.name.text.toString() == ""){
                it.name.let(binding.name::setText)
            }
            if (binding.position.text.toString() ==""){
                it.position.let(binding.position::setText)
            }
            start.let(binding.start::setText)
            end.let(binding.end::setText)
            if (binding.link.text.toString() == "") {
                it.link.let(binding.link::setText)
            }
        }

        binding.start.setOnTouchListener { viewStart, event ->
            viewStart.isFocusable = true
            startEndFinished = true
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val layout: Layout = (viewStart as EditText).layout
                    val x: Float = event.x + viewStart.scrollX
                    val offset: Int = layout.getOffsetForHorizontal(0, x)
                    if (offset > 0) if (x > layout.getLineMax(0)) viewStart.setSelection(
                        offset
                    ) else viewStart.setSelection(offset - 1)
                    getDataCalendar()
                    DatePickerDialog(context!!, this, year, month, day).show()
                }
            }
            true
        }

        binding.end.setOnTouchListener { v, event ->
            v.isFocusable = true
            startEndFinished = false
                when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val layout: Layout = (v as EditText).layout
                    val x: Float = event.x + v.scrollX
                    val offset: Int = layout.getOffsetForHorizontal(0, x)
                    if (offset > 0) if (x > layout.getLineMax(0)) v.setSelection(
                        offset
                    ) else v.setSelection(offset - 1)
                    getDataCalendar()
                    DatePickerDialog(context!!, this, year, month, day).show()

                }
            }
            true
        }

        binding.enter.setOnClickListener {
            AndroidUtils.hideKeyboard(requireView())
            if (binding.name.text.toString() =="" || binding.position.text.toString()=="" || binding.start.text.toString() == "") {
                Snackbar.make(binding.root, R.string.first_three, Snackbar.LENGTH_SHORT).show()
            } else {
                val job = Job(
                    id = 0,
                    name = binding.name.text.toString(),
                    position = binding.position.text.toString(),
                    start = viewModel.editedJob.value!!.start,
                    finish = viewModel.editedJob.value!!.finish,
                    link = if (binding.link.text.toString() == "") {null} else {binding.link.text.toString()}
                )
                viewModel.editJob(job)
                viewModel.addJob()
            }
        }

        viewModel.postCreated.observe(viewLifecycleOwner) {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun getDataCalendar(){
        if (day == 0){
            val cal = Calendar.getInstance()
            day = cal.get(Calendar.DAY_OF_MONTH)
            month = cal.get(Calendar.MONTH)
            year  = cal.get(Calendar.YEAR)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(view: DatePicker?, yearOf: Int, monthOf: Int, dayOfMonth: Int) {
        day = dayOfMonth
        month = monthOf
        year = yearOf
        val date = listOf(day, month, year)
        val dateTime = GoDataTime.convertDataInput(date)
        if (startEndFinished){
            viewModel.addDateStart(dateTime)
        } else {
            viewModel.addDateFinish(dateTime)
        }
    }
}