package ru.maxpek.friendslinkup.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ru.maxpek.friendslinkup.adapter.AdapterCallback
import ru.maxpek.friendslinkup.adapter.ListOfUsersAdapter
import ru.maxpek.friendslinkup.databinding.FaragmenListOfUsersBinding
import ru.maxpek.friendslinkup.viewmodel.PostViewModel

class ListOfMentions : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FaragmenListOfUsersBinding.inflate(inflater, container, false)

        val postViewModel: PostViewModel by activityViewModels()


        val adapter = ListOfUsersAdapter(object : AdapterCallback {
            override fun isChecked(id: Int) {

            }
            override fun unChecked(id: Int) {

            }
        })
        binding.list.adapter = adapter

        postViewModel.data.observe(viewLifecycleOwner) {
            val newUser = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newUser) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.enter.setOnClickListener {
            newPostViewModel.addMentionIds()
            findNavController().navigateUp()
        }
        return binding.root
    }

}