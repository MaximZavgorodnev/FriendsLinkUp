package ru.maxpek.friendslinkup

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppActivity : AppCompatActivity() {
    private lateinit var bottomNavView: BottomNavigationView

    @SuppressLint("RestrictedApi", "ResourceType", "UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var binding: Bindin
        setContentView(binding.root)
        hideActionBar()

    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}