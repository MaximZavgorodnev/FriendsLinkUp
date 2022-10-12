package ru.maxpek.friendslinkup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxpek.friendslinkup.databinding.CardJobsBinding
import ru.maxpek.friendslinkup.dto.Job
import ru.maxpek.friendslinkup.util.GoDataTime


class UserWallJobAdapter : ListAdapter<Job,
        UserWallViewHolder>(UserWallJobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWallViewHolder {
        val binding = CardJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserWallViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserWallViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}

class UserWallViewHolder(
    private val binding: CardJobsBinding,
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("NewApi")
    fun bind(job: Job) {
        binding.apply {
            nameOrganization.text = job.name
            position.text = job.position
            start.text = GoDataTime.convertDataTimeJob(job.start)
            finish.text = job.finish?.let { GoDataTime.convertDataTimeJob(it) }
            link.text = job.link
            menu.visibility = View.GONE
        }
    }
}

class UserWallJobDiffCallback : DiffUtil.ItemCallback<Job>() {
    override fun areItemsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Job, newItem: Job): Boolean {
        return oldItem == newItem
    }
}