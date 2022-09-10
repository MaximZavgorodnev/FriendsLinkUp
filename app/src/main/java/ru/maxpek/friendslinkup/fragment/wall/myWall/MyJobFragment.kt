package ru.maxpek.friendslinkup.fragment.wall.myWall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.databinding.FragmentMyWallJobBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyJobFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyWallJobBinding.inflate(inflater, container, false)



        return binding.root
    }
}