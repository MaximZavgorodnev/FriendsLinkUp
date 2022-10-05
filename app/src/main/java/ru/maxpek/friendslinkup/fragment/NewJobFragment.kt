package ru.maxpek.friendslinkup.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.method.DateTimeKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.DatePicker
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
import ru.maxpek.friendslinkup.util.GoDataTime
import ru.maxpek.friendslinkup.viewmodel.JobViewModel
import java.util.*


var day = 0
var month = 0
var year = 0

var savedDay = 0
var savedMonth = 0
var savedYear = 0
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class NewJobFragment: Fragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: JobViewModel by activityViewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("FragmentBackPressedCallback", "UseRequireInsteadOfGet")
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

        pickDate()

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


        binding.start.setOnEditorActionListener { textView, actionId, keyEvent ->

            if (textView.isInputMethodTarget){
                println("111111100000000")
            }
            println(keyEvent)
//           if (actionId == EditorInfo.){
//               println("111111100000000")
//           }
//            println("111111100000000")
            true
        }

        binding.button.setOnClickListener {
            getDataCalendar()
            DatePickerDialog(context!!, this, year, month, day).show()

        }
        return binding.root
    }

    private fun getDataCalendar(){
        val cal = Calendar.getInstance()
        day   = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year  = cal.get(Calendar.YEAR)
    }

    private fun pickDate(){

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        println("$savedDay  $savedMonth  $savedYear  " )
    }
}