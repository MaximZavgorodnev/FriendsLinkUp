package ru.maxpek.friendslinkup.fragment.dialog

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageIntentChooser
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.util.FileUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentRegistrationBinding
import ru.maxpek.friendslinkup.dto.UserRegistration
import ru.maxpek.friendslinkup.util.AndroidUtils
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import java.io.File

private const val FILE_NAME = "photo.jpg"
@AndroidEntryPoint
class RegistrationFragment : DialogFragment() {

    private var imageResultLauncher: ActivityResultLauncher<Intent>? = null
    private var cameraResultLauncher: ActivityResultLauncher<Intent>? = null
    //    private var cropImage: CropImageIntentChooser.ResultCallback? = null


    private val viewModel: AuthViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        var file: File? = null

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
                }
            }
        }
        imageResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result?.data?.data
                if (uri != null) {
                    binding.avatar.setImageURI(uri)
                } else {
                    binding.avatar.setImageResource(R.mipmap.ic_launcher)
                }
            }


//                val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

//                filePhoto = getPhotoFile(FILE_NAME)
//                val providerFile = FileProvider.getUriForFile(this,"com.example.androidcamera.fileprovider", filePhoto)
//                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerFile)
        }









        cameraResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val image = result?.data?.extras?.get("data") as Bitmap
                binding.avatar.setImageBitmap(image)
            }
        }




        val cropImage = registerForActivityResult(CropImageContract()) { result ->
            if (result.isSuccessful) {
                // use the returned uri
                val uriContent = result.uriContent
//                val uriFilePath = result.getUriFilePath(activity!!.application) // optional usage
                file = File(uriContent.toString())
                binding.avatar.setImageURI(uriContent)
            } else {
                // an error occurred
                val exception = result.error
            }
        }

        binding.addImage.setOnClickListener {
            cropImage.launch(
                options {
                    setMaxCropResultSize(400,400)
                    setGuidelines(CropImageView.Guidelines.ON)
                }
            )
//            PopupMenu(it.context, it).apply {
//                inflate(R.menu.menu_add_image)
//                setOnMenuItemClickListener { item ->
//                    when (item.itemId) {
//                        R.id.image -> {
////                            val intent = Intent(Intent.ACTION_PICK)
////                            intent.type = "image/*"
////                            imageResultLauncher!!.launch(intent)
//
//
//                            true
//                        }
//                        R.id.camera -> {
//                            val bInt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                            cameraResultLauncher!!.launch(bInt)
//                            true
//                        }
//
//                        else -> false
//                    }
//                }
//            }.show()
        }

        binding.deleteImage.setOnClickListener {
            binding.avatar.setImageResource(R.mipmap.ic_launcher)
        }
        return binding.root
    }


}
