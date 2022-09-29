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
                            findNavController().navigate(R.id.myEventFragment2)
                            true
                        }

                        R.id.jobs -> {
                            Snackbar.make(binding.root, R.string.you_are_where, Snackbar.LENGTH_SHORT).show()
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