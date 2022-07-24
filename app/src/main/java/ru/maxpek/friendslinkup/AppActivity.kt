package ru.maxpek.friendslinkup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.AndroidEntryPoint
import ru.maxpek.friendslinkup.auth.AppAuth
import ru.maxpek.friendslinkup.databinding.ActivityAppBinding
import ru.maxpek.friendslinkup.fragment.EventFragment
import ru.maxpek.friendslinkup.fragment.FeedFragment
import ru.maxpek.friendslinkup.fragment.JobFragment
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
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityAppBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey(MAPKIT_API_KEY)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideActionBar()
        bottomNavView = binding.bottomNavigation

        val feedFragment = FeedFragment()
        val eventFragment = EventFragment()
        val jobFragment = JobFragment()

        setThatFragment(feedFragment)

        bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.posts ->{
                    setThatFragment(feedFragment)
                }
                R.id.jobs ->{
                    setThatFragment(jobFragment)
                }
                R.id.events ->{
                    setThatFragment(eventFragment)
                }
            }
            true
        }

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                menuInflater.inflate(R.menu.menu_main, menu)
                menu.let {menuVisible ->
                    menuVisible.setGroupVisible(R.id.unauthenticated, !viewModel.authenticated)
                    menuVisible.setGroupVisible(R.id.authenticated, viewModel.authenticated)
            }
//                inflate(R.menu.menu_main)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
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
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        viewModel.data.observe(this) {
            invalidateOptionsMenu()
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

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun setThatFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.frame, fragment)
        commit()
    }
}