package ru.maxpek.friendslinkup.fragment.wall.myWall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.jobs.MyJobsAdapter
import ru.maxpek.friendslinkup.adapter.jobs.OnClickListener
import ru.maxpek.friendslinkup.databinding.FragmentMyWallJobBinding
import ru.maxpek.friendslinkup.dto.Job
import ru.maxpek.friendslinkup.viewmodel.AuthViewModel
import ru.maxpek.friendslinkup.viewmodel.JobViewModel

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MyJobFragment: Fragment() {
    private val authViewModel: AuthViewModel by viewModels()
    private val viewModel: JobViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMyWallJobBinding.inflate(inflater, container, false)
        viewModel.getMyJob()

        val adapter = MyJobsAdapter(object : OnClickListener {
            override fun onEditJob(job: Job) {
                viewModel.editJob(job)
                findNavController().navigate(R.id.newJobFragment)
            }

            override fun onRemoveJob(job: Job) {
                viewModel.removeById(job.id)
            }
        })

        binding.list.adapter = adapter

        viewModel.data.observe(viewLifecycleOwner) {
            val newJob = adapter.itemCount < it.size
            adapter.submitList(it) {
                if (newJob) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.newJobFragment)
        }

        binding.menu.setOnClickListener {
            findNavController().navigate(R.id.myPostFragment)
        }

        binding.home.setOnClickListener {
            findNavController().navigate(R.id.feedFragment)
        }

        return binding.root
    }
}