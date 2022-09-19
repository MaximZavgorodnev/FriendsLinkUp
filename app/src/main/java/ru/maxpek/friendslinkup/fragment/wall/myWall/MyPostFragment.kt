package ru.maxpek.friendslinkup.fragment.wall.myWall

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
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentMyWallPostBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyPostFragment(id: Int): Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyWallPostBinding.inflate(inflater, container, false)

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {

                inflate(R.menu.menu_navigation)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.posts -> {
                            Snackbar.make(binding.root, R.string.you_are_where, Snackbar.LENGTH_SHORT).show()
                            true
                        }
                        R.id.events -> {
                            findNavController().navigate(R.id.action_myPostFragment_to_myEventFragment2)
                            true
                        }

                        R.id.jobs -> {
                            findNavController().navigate(R.id.action_myPostFragment_to_myJobFragment2)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_myPostFragment_to_feedFragment)
        }



        return binding.root
    }
}