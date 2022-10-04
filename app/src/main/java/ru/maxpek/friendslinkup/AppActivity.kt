package ru.maxpek.friendslinkup

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavAction
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.databinding.ActivityAppBinding
import ru.maxpek.friendslinkup.databinding.ActivityAppBinding.inflate
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    @Inject
    lateinit var appAuth: AppAuth
    @Inject
    lateinit var firebaseMessaging: FirebaseMessaging
    @Inject
    lateinit var googleApiAvailability: GoogleApiAvailability

    companion object{
        private const val MAPKIT_API_KEY = "e0f40ead-fefb-45cf-821c-37efc0eaa548"
    }
    lateinit var binding: ActivityAppBinding

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        binding = inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_24)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected

            when(menuItem.itemId){
                R.id.posts ->{
                    findNavController(R.id.frame).navigate(R.id.feedFragment)
                }
                R.id.events ->{
                    findNavController(R.id.frame).navigate(R.id.eventFragment)
                }
            }
            binding.drawer.closeDrawer(GravityCompat.START)
            menuItem.isChecked = true
            true
        }

        viewModel.data.observe(this) {
            invalidateOptionsMenu()
        }
        lifecycleScope.launchWhenCreated {
            appAuth.authStateFlow.collectLatest{
                val textName: TextView = binding.navigationView.getHeaderView(0).findViewById(R.id.nameUser)
                textName.text = it.nameUser

                Glide.with(binding.navigationView)
                    .load(it.avatarUser)
                    .error(R.drawable.ic_avatar_loading_error_48)
                    .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                    .timeout(10_000)
                    .circleCrop()
                    .into(binding.navigationView.getHeaderView(0).findViewById(R.id.avatarDr))
            }
        }

        binding.navigationView.getHeaderView(0).setOnClickListener {
            findNavController(R.id.frame).navigate(R.id.myPostFragment)
            binding.drawer.closeDrawer(GravityCompat.START)
        }

        checkGoogleApiAvailability()
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        menu.let {
            it.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
            it.setGroupVisible(R.id.authenticated, viewModel.authenticated)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        binding.drawer.isOpen
        return when (item.itemId) {
            R.id.signin -> {
                findNavController(R.id.frame).navigate(R.id.action_feedFragment_to_authenticationFragment)
                true
            }
            R.id.signup -> {
                findNavController(R.id.frame).navigate(R.id.action_feedFragment_to_registrationFragment)
                true
            }
            R.id.signout -> {
                appAuth.removeAuth()
                findNavController(R.id.frame).navigateUp()
                true
            }
            android.R.id.home -> {
                if (binding.drawer.isOpen) binding.drawer.closeDrawer(GravityCompat.START) else binding.drawer.openDrawer(GravityCompat.START)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    private fun checkGoogleApiAvailability() {
        with(googleApiAvailability) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        firebaseMessaging.token.addOnSuccessListener {
            println(it)
        }
    }
}

