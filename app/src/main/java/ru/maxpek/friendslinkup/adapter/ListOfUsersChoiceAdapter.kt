package ru.maxpek.friendslinkup.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.CardMentionsCheckedBinding
import ru.maxpek.friendslinkup.dto.UserRequested

interface AdapterCallback {
    fun isChecked(id: Int) {}
    fun unChecked(id: Int) {}
}

class ListOfUsersChoiceAdapter (private val callback: AdapterCallback) :
    ListAdapter<UserRequested, UsersListOfViewHolder>(UsersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersListOfViewHolder {
        val binding = CardMentionsCheckedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsersListOfViewHolder (binding, callback)
    }

    override fun onBindViewHolder(holder: UsersListOfViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class UsersListOfViewHolder
    (private val binding: CardMentionsCheckedBinding,
     private val callback: AdapterCallback)  : RecyclerView.ViewHolder(binding.root) {

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

            checkbox.apply {
                isChecked = user.checked
            }

            checkbox.setOnClickListener {
                if (checkbox.isChecked) {
                    callback.isChecked(user.id)
                } else {
                    callback.unChecked(user.id)
                }

            }
        }
    }
}

class UsersDiffCallback : DiffUtil.ItemCallback<UserRequested>() {
    override fun areItemsTheSame(oldItem: UserRequested, newItem: UserRequested): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UserRequested, newItem: UserRequested): Boolean {
        return oldItem == newItem
    }
}