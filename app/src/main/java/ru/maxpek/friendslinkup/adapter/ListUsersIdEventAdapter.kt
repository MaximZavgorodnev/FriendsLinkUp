package ru.maxpek.friendslinkup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.CardIdsBinding
import ru.maxpek.friendslinkup.dto.UserRequested

class ListUsersIdEventAdapter(private val callback: AdapterUsersIdCallback) :
    ListAdapter<UserRequested, UsersListEventViewHolder>(UsersListIdEventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListEventViewHolder {
        val binding = CardIdsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersListEventViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: UsersListEventViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class UsersListEventViewHolder(
    private val binding: CardIdsBinding,
    private val callback: AdapterUsersIdCallback
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserRequested) {
        binding.apply {
            Glide.with(binding.root)
                .load(user.avatar)
                .error(R.drawable.ic_avatar_loading_error_48)
                .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)
            this.avatar.setOnClickListener {
                callback.goToPageUser(user.id)
            }
        }
    }
}

class UsersListIdEventDiffCallback : DiffUtil.ItemCallback<UserRequested>() {
    override fun areItemsTheSame(oldItem: UserRequested, newItem: UserRequested): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserRequested, newItem: UserRequested): Boolean {
        return oldItem == newItem
    }
}