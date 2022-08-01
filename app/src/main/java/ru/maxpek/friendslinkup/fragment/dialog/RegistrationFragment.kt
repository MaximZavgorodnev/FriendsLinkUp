package ru.maxpek.friendslinkup.fragment.dialog



import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toFile
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.canhub.cropper.*
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentRegistrationBinding
import ru.maxpek.friendslinkup.dto.PhotoModel
import ru.maxpek.friendslinkup.dto.UserRegistration
import ru.maxpek.friendslinkup.util.AndroidUtils
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel


@AndroidEntryPoint
class RegistrationFragment : DialogFragment() {

    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        var file: PhotoModel? = null

        val enter = binding.enter

        enter.setOnClickListener {
            val usernameEditText = binding.username.text.toString()
            val loginEditText = binding.login.text.toString()
            val passwordEditText = binding.password.text.toString()
            val repeatPasswordEditText = binding.repeatPassword.text.toString()
            val avatarImage = file
            if (passwordEditText != repeatPasswordEditText){
                Snackbar.make(binding.root, R.string.password_not_match, Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                if (loginEditText == "" || passwordEditText == "") {
                    Snackbar.make(binding.root, R.string.All_fields, Snackbar.LENGTH_SHORT).show()
                    return@setOnClickListener
                } else {
                    val user = UserRegistration(loginEditText, passwordEditText, usernameEditText, avatarImage)
                    viewModel.onSignUp(user)
                    AndroidUtils.hideKeyboard(requireView())
//                    findNavController().navigateUp()
//                    findNavController().navigate(R.id.action_registrationFragment_to_feedFragment)
                }
            }
        }

        val cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                val uriContent = result.uriContent
                val uriFilePath = result.getUriFilePath(activity!!) // optional usage

                file = PhotoModel(uriContent,
                    uriFilePath?.toUri()?.toFile())
                binding.avatar.setImageURI(uriContent)
            } else {
                // an error occurred
                val exception = result.error
            }
        }

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
                        file = PhotoModel(uri, uri?.toFile())
                        binding.avatar.setImageURI(uri)
                        viewModel.changePhoto(uri, uri?.toFile())
                    }
                }
            }


        binding.avatar.setOnClickListener {
//            cropImage.launch(
//                options {
//                    setAspectRatio(1,1)
//                    setRequestedSize(600,600)
//                    setCropShape(CropImageView.CropShape.OVAL)
//                    setGuidelines(CropImageView.Guidelines.ON)
//                }
//            )

            ImagePicker.with(this)
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

        binding.deleteImage.setOnClickListener {
            binding.avatar.setImageResource(R.mipmap.ic_launcher)
        }
        return binding.root
    }


}
