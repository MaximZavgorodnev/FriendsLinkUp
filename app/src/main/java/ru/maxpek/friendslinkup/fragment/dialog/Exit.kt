package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import ru.maxpek.friendslinkup.databinding.FragmentExitBinding
import kotlin.system.exitProcess

class Exit: DialogFragment()  {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentExitBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            ActivityCompat.finishAffinity(requireActivity());
            exitProcess(0);
        }

        binding.button2.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
}