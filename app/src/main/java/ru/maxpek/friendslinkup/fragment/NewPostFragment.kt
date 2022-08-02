package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentNewPostBinding

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNewPostBinding.inflate(
            inflater,
            container,
            false
        )

        binding.menu.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_add_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.mention -> {
                            findNavController().navigate(R.id.action_newPostFragment_to_listOfUsers)
                            true
                        }
                        R.id.photoVideo -> {

                            true
                        }
                        R.id.geo -> {
                            findNavController().navigate(R.id.action_newPostFragment_to_mapsFragment)
                            true
                        }
                        R.id.link -> {
                            findNavController().navigate(R.id.action_newPostFragment_to_addLink)
                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        return binding.root
    }
}