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
            it.name.let(binding.name::setText)
            it.position.let(binding.position::setText)
            it.start.apply { GoDataTime.convertDataTimeJob(this) }.let(binding.start::setText)
            it.finish.apply { GoDataTime.convertDataTimeJob(this) }.let(binding.end::setText)
            it.link.let(binding.link::setText)
        }
        viewModel.dateStart.observe(viewLifecycleOwner) {
            it.let(binding.start::setText)
        }

        viewModel.dateFinish.observe(viewLifecycleOwner) {
            it.let(binding.end::setText)
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
            val job = Job(
                id = 0,
                name = binding.name.text.toString(),
                position = binding.position.text.toString(),
                start = GoDataTime.convertDateToLocalDate(binding.start.text.toString()),
                finish = GoDataTime.convertDateToLocalDate(binding.end.text.toString()),
                link = binding.link.text.toString()
            )
            viewModel.editJob(job)
            viewModel.addJob()
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
//        val kkk = GoDataTime.convertDateToLocalDate(dateTime)
//        val dateFormated = SimpleDateFormat("dd/MM/yyyy").format(trans.created_date.toDate())
//        println(kkk)
//        val current = LocalDateTime.now()
        println(dateTime)

    }
}