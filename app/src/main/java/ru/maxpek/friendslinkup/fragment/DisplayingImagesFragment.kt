package ru.maxpek.friendslinkup.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import ru.maxpek.friendslinkup.R
import ru.maxpek.friendslinkup.databinding.FragmentDisplayingImagesBinding
import ru.maxpek.friendslinkup.util.StringArg

class DisplayingImagesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDisplayingImagesBinding.inflate(
            inflater,
            container,
            false
        )

        val url = arguments?.textArg
        Glide.with(this)
            .load(url)
            .error(R.drawable.ic_avatar_loading_error_48)
            .placeholder(R.drawable.ic_baseline_cruelty_free_48)
            .timeout(10_000)
            .into(binding.imageView)

        return binding.root
    }

    companion object {
        var Bundle.textArg: String? by StringArg
    }
}