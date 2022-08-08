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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentNewPostBinding
import ru.maxpek.friendslinkup.util.ArrayInt
import ru.maxpek.friendslinkup.util.PointArg
import ru.maxpek.friendslinkup.viewmodel.NewPostViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
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
        val newPostViewModel : NewPostViewModel by viewModels()


        binding.menuAdd.setOnClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.menu_add_post)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.audio -> {

                            true
                        }
                        R.id.video -> {

                            true
                        }
                        R.id.image -> {

                            true
                        }

                        else -> false
                    }
                }
            }.show()
        }

        binding.mentionAdd.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_listOfUsers)
        }

        binding.geoAdd.setOnClickListener {
            findNavController().navigate(R.id.action_newPostFragment_to_mapsFragment)
        }

        binding.linkAdd.setOnClickListener {
            binding.editLink.visibility = View.VISIBLE
            binding.okAdd.visibility = View.VISIBLE
        }

        binding.okAdd.setOnClickListener {
            binding.editLink.visibility = View.GONE
            binding.okAdd.visibility = View.GONE
        }

        newPostViewModel.newPost.observe(viewLifecycleOwner) {
            binding.mentionAdd.isChecked = it.mentionIds.isNotEmpty()
        }



        return binding.root
    }

    companion object {
        var Bundle.arrayInt: List<Int>? by ArrayInt
        var Bundle.pointArg: Point by PointArg
    }
}