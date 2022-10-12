package ru.maxpek.friendslinkup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.CardIdsBinding
import ru.maxpek.friendslinkup.databinding.CardMentionBinding
import ru.maxpek.friendslinkup.dto.UserRequested


class UsersListAdapter(private val callback: AdapterUsersIdCallback) :
    ListAdapter<UserRequested, UsersListViewHolder>(UsersListIdDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListViewHolder {
        val binding = CardMentionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersListViewHolder(binding, callback)
    }

    override fun onBindViewHolder(holder: UsersListViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class UsersListViewHolder(
    private val binding: CardMentionBinding,
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
            author.text = user.name
            avatar.setOnClickListener {
                callback.goToPageUser(user.id)
            }
            author.setOnClickListener {
                callback.goToPageUser(user.id)
            }
        }
    }
}

class UsersListIdDiffCallback : DiffUtil.ItemCallback<UserRequested>() {
    override fun areItemsTheSame(oldItem: UserRequested, newItem: UserRequested): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserRequested, newItem: UserRequested): Boolean {
        return oldItem == newItem
    }
}