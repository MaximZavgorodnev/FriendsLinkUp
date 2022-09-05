package ru.maxpek.friendslinkup.adapter.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.adapter.AdapterUsersIdCallback
import ru.maxpek.friendslinkup.adapter.ListUsersIdAdapter
import ru.maxpek.friendslinkup.databinding.CardPostBinding
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.enumeration.AttachmentType.*
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg

interface OnInteractionListener {
    fun onLike(post: PostResponse) {}
    fun onEdit(post: PostResponse) {}
    fun onRemove(post: PostResponse) {}
    fun onShare(post: PostResponse) {}
    fun loadingTheListOfMentioned(mentionIds: List<Int>) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : PagingDataAdapter<PostResponse, PostViewHolder>(PostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    private val adapter = ListUsersIdAdapter(object : AdapterUsersIdCallback {
        override fun goToPageUser() {}
    })

    fun bind(post: PostResponse) {
        binding.apply {
            group.visibility = View.GONE
            mentionedMe.visibility = View.GONE
            mentionIds.adapter = adapter
            onInteractionListener.loadingTheListOfMentioned(post.mentionIds)

                Glide.with(itemView)
                .load(post.authorAvatar)
                .error(R.drawable.ic_avatar_loading_error_48)
                .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)


            if (post.attachment?.url != "") {
                when (post.attachment?.type) {
                    IMAGE -> {
                        group.visibility = View.VISIBLE
                        video.visibility = View.GONE
                    }
                    VIDEO ->{group.visibility = View.VISIBLE}
                    AUDIO ->{}
                    null -> {group.visibility = View.GONE}
                }
                group.visibility = View.VISIBLE
                Glide.with(itemView).load(post.attachment?.url).timeout(10_000).into(backgroundVideo)
            }

            author.text = post.author
            published.text = post.published
            content.text = post.content
            like.isChecked = post.likedByMe
            like.text = "${post.likeOwnerIds.size}"
            geo.isChecked = post.coords != null
            mentionedMe.visibility = if (post.mentionedMe) View.VISIBLE else View.INVISIBLE
            menu.visibility = if (post.ownerByMe) View.VISIBLE else View.INVISIBLE

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            share.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            backgroundVideo.setOnClickListener{
                it.findNavController().navigate(R.id.action_eventFragment_to_displayingImagesFragment2,
                    Bundle().apply { textArg = post.attachment?.url ?: " "})
            }

        }
    }
}

class PostDiffCallback : DiffUtil.ItemCallback<PostResponse>() {
    override fun areItemsTheSame(oldItem: PostResponse, newItem: PostResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostResponse, newItem: PostResponse): Boolean {
        return oldItem == newItem
    }
}