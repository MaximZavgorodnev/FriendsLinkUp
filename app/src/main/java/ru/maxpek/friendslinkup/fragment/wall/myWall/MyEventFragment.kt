package ru.maxpek.friendslinkup.fragment.wall.myWall

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentMyWallEventBinding
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.MyWallPostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyEventFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMyWallEventBinding.inflate(inflater, container, false)



        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_navigation)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.posts -> {
                            findNavController().navigate(R.id.myPostFragment)
                            true
                        }
                        R.id.events -> {
                            Snackbar.make(binding.root, R.string.you_are_where, Snackbar.LENGTH_SHORT).show()
                            true
                        }

                        R.id.jobs -> {
                            findNavController().navigate(R.id.myJobFragment2)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        binding.home.setOnClickListener {
            findNavController().navigate(R.id.feedFragment)
        }

        return binding.root
    }
}