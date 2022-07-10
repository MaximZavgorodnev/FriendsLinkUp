package ru.maxpek.friendslinkup

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class AppActivity : AppCompatActivity() {
    @SuppressLint("RestrictedApi", "ResourceType", "UseSupportActionBar")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hideActionBar()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }
}