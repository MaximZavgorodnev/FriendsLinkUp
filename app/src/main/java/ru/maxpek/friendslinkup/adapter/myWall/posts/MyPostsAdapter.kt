@file:OptIn(ExperimentalCoroutinesApi::class)

package ru.maxpek.friendslinkup.adapter.myWall.posts

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yandex.mapkit.geometry.Point
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.CardPostBinding
import ru.maxpek.friendslinkup.dto.PostResponse
import ru.maxpek.friendslinkup.enumeration.AttachmentType
import ru.maxpek.friendslinkup.fragment.DisplayingImagesFragment.Companion.textArg
import ru.maxpek.friendslinkup.fragment.FeedFragment.Companion.intArg
import ru.maxpek.friendslinkup.fragment.MapsFragment.Companion.pointArg
import ru.maxpek.friendslinkup.util.GoDataTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface MyWallOnInteractionListener {
    fun onLike(post: PostResponse) {}
    fun onEdit(post: PostResponse) {}
    fun onRemove(post: PostResponse) {}
    fun onShare(post: PostResponse) {}
    fun loadingTheListOfMentioned(post: PostResponse) {}
}

class MyPostsAdapter(
    private val onInteractionListener: MyWallOnInteractionListener,
) : PagingDataAdapter<PostResponse, MyWallPostViewHolder>(MyWallPostDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyWallPostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyWallPostViewHolder(binding, onInteractionListener)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyWallPostViewHolder, position: Int) {
        val post = getItem(position) ?: return
        holder.bind(post)
    }
}


class MyWallPostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: MyWallOnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(post: PostResponse) {
        binding.apply {
            mentionedMe.visibility = View.GONE
            video.visibility = View.GONE
            backgroundVideo.visibility = View.GONE

            Glide.with(itemView)
                .load(post.authorAvatar)
                .error(R.drawable.ic_avatar_loading_error_48)
                .placeholder(R.drawable.ic_baseline_cruelty_free_48)
                .timeout(10_000)
                .circleCrop()
                .into(avatar)


            if (post.attachment?.url != "") {
                when (post.attachment?.type) {
                    AttachmentType.IMAGE -> {
                        backgroundVideo.visibility = View.VISIBLE
                    }
                    AttachmentType.VIDEO -> {
                        video.visibility = View.GONE
                        backgroundVideo.visibility = View.GONE
                    }
                    AttachmentType.AUDIO -> {}
                    null -> {
                        video.visibility = View.GONE
                        backgroundVideo.visibility = View.GONE
                    }
                }
                Glide.with(itemView).load(post.attachment?.url).timeout(10_000)
                    .into(backgroundVideo)
            }


            author.text = post.author
            published.text = GoDataTime.convertDataTime(post.published)
            content.text = post.content + if (post.link != null) {
                "\n" + post.link
            } else {
                ""
            }
            like.isChecked = post.likedByMe
            like.text = "${post.likeOwnerIds.size}"
            geo.visibility = if (post.coords != null) View.VISIBLE else View.INVISIBLE
            mentionedMe.visibility = if (post.mentionedMe) View.VISIBLE else View.INVISIBLE
            menu.visibility = if (post.ownerByMe) View.VISIBLE else View.INVISIBLE
            mentions.text = "${post.mentionIds.size}"

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

            backgroundVideo.setOnClickListener {
                it.findNavController().navigate(
                    R.id.displayingImagesFragment2,
                    Bundle().apply { textArg = post.attachment?.url ?: " " })
            }
            mentions.setOnClickListener {
                onInteractionListener.loadingTheListOfMentioned(post)
            }
            content.setOnClickListener {
                it.findNavController().navigate(
                    R.id.openPostFragment2,
                    Bundle().apply { intArg = post.id })
            }
            geo.setOnClickListener {
                it.findNavController().navigate(R.id.mapsFragment,
                    Bundle().apply {
                        Point(
                            post.coords?.lat!!.toDouble(), post.coords.long.toDouble()
                        ).also { pointArg = it }
                    })
            }
        }
    }
}

class MyWallPostDiffCallback : DiffUtil.ItemCallback<PostResponse>() {
    override fun areItemsTheSame(oldItem: PostResponse, newItem: PostResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostResponse, newItem: PostResponse): Boolean {
        return oldItem == newItem
    }
}