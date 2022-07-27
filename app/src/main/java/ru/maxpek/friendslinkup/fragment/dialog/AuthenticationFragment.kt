package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentAuthenticationBinding
import ru.maxpek.friendslinkup.dto.UserResponse
import ru.maxpek.friendslinkup.util.AndroidUtils
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel

@AndroidEntryPoint
class AuthenticationFragment : DialogFragment() {

    private val viewModel: AuthViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentAuthenticationBinding.inflate(inflater, container, false)

        val enter = binding.enter

        enter.setOnClickListener {
            val usernameEditText = binding.username.text.toString()
            val passwordEditText = binding.password.text.toString()
            if (usernameEditText=="" || passwordEditText==""){
                Snackbar.make(binding.root, R.string.All_fields, Snackbar.LENGTH_SHORT).show()
            } else {
                val userResponse = UserResponse(usernameEditText, passwordEditText)
                viewModel.onSignIn(userResponse)
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }

        return binding.root

    }




}