package ru.maxpek.friendslinkup.fragment.wall.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.OnClickListener
import ru.maxpek.friendslinkup.adapter.UserWallJobAdapter
import ru.maxpek.friendslinkup.auth.AppAuth.Companion.avatar
import ru.maxpek.friendslinkup.databinding.FragmentUserWallJobBinding
import ru.maxpek.friendslinkup.util.StringArg
import ru.maxpek.friendslinkup.viewmodel.UserWallViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class UserJobFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserWallJobBinding.inflate(inflater, container, false)
        val userWallViewModel: UserWallViewModel by activityViewModels()
        if (arguments != null){
            arguments?.textArg?.let {
                userWallViewModel.getUser(it)
                userWallViewModel.getJobUser(it)
            }
        }


        val adapter = UserWallJobAdapter(object : OnClickListener{

        })

        binding.list.adapter = adapter
        userWallViewModel.data.observe(viewLifecycleOwner) {
            val newJob = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newJob) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }
        userWallViewModel.userData.observe(viewLifecycleOwner) {
            binding.nameUser.text = it.name
            binding.avatar

            Glide.with(this)
                .load(it.avatar)
                .error(R.drawable.ic_avatar_loading_error_48)
                .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                .timeout(10_000)
                .circleCrop()
                .into(binding.avatar)
        }
        binding.home.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}