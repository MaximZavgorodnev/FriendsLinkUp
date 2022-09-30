package ru.maxpek.friendslinkup.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.maxpek.friendslinkup.databinding.CardJobsBinding
import ru.maxpek.friendslinkup.dto.JobResponse
import ru.maxpek.friendslinkup.util.GoDataTime

interface OnClickListener{

}

class UserWallJobAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<JobResponse, UserWallViewHolder>(UserWallJobDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserWallViewHolder {
        val binding = CardJobsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserWallViewHolder (binding, onClickListener)
    }

    override fun onBindViewHolder(holder: UserWallViewHolder, position: Int) {
        val job = getItem(position)
        holder.bind(job)
    }
}

class UserWallViewHolder(
    private val binding: CardJobsBinding,
    private val onClickListener: OnClickListener
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("NewApi")
    fun bind(job: JobResponse){
        binding.apply {
            nameOrganization.text = job.name
            position.text = job.position
            start.text = GoDataTime.convertDataTimeJob(job.start)
            finish.text = GoDataTime.convertDataTimeJob(job.finish)
            link.text = job.link
            menu.visibility = View.GONE
        }
    }
}

class UserWallJobDiffCallback : DiffUtil.ItemCallback<JobResponse>() {
    override fun areItemsTheSame(oldItem: JobResponse, newItem: JobResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: JobResponse, newItem: JobResponse): Boolean {
        return oldItem == newItem
    }
}