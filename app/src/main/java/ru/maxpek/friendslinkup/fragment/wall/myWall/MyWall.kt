package ru.maxpek.friendslinkup.fragment.wall.myWall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentUserBinding

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyWall: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentUserBinding.inflate(inflater, container, false)

//        val postFragment = MyPostFragment()
//        val eventFragment = MyEventFragment()
//        val jobFragment = MyJobFragment()
        binding.appBarLayout

//        binding.bottomNavigation.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.posts -> {
////                    binding.frame.setThatFragment(postFragment)
//
////                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.action_myWall2_to_myPostFragment)
//                    true
//                }
//                R.id.events -> {
////                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
//                    findNavController().navigate(R.id.action_myWall2_to_myEventFragment2)
//                    true
//                }
//                R.id.jobs -> {
////                    Snackbar.make(binding.root, R.string.To_continue, Snackbar.LENGTH_SHORT).show()
////                    findNavController().navigate(R.id.action_myPostFragment2_to_myJobFragment2)
//                    true
//                }
//                else -> false
//            }
//
//        }





            return binding.root
    }
}