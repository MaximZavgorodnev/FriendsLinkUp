package ru.maxpek.friendslinkup

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.databinding.ActivityAppBinding
import ru.maxpek.friendslinkup.fragment.EventFragment
import ru.maxpek.friendslinkup.fragment.FeedFragment
import ru.maxpek.friendslinkup.fragment.JobFragment

class AppActivity : AppCompatActivity() {

    companion object{
        private const val MAPKIT_API_KEY = "e0f40ead-fefb-45cf-821c-37efc0eaa548"
    }
    private lateinit var bottomNavView: BottomNavigationView
    lateinit var binding: ActivityAppBinding

    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("RestrictedApi", "ResourceType", "UseSupportActionBar")
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


    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun setThatFragment(fragment: Fragment) = supportFragmentManager.beginTransaction().apply {
        replace(R.id.frame, fragment)
        commit()
    }
}