package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yandex.mapkit.geometry.Point
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentNewPostBinding
import ru.maxpek.friendslinkup.util.ArrayInt
import ru.maxpek.friendslinkup.util.PointArg
import ru.maxpek.friendslinkup.viewmodel.ListOfUserViewModel

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
        val listOfUserViewModel : ListOfUserViewModel by viewModels(ownerProducer = ::requireParentFragment)

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

    companion object {
        var Bundle.arrayInt: List<Int>? by ArrayInt
        var Bundle.pointArg: Point by PointArg
    }
}