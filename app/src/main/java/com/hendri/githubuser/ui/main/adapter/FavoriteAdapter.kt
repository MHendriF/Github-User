package com.hendri.githubuser.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hendri.githubuser.R
import com.hendri.githubuser.data.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FavoriteAdapter(
    private val listener: (User) -> Unit
) : ListAdapter<User, FavoriteAdapter.ItemViewholder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User?>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.login.equals(newItem.login) &&
                        oldItem.html_url.equals(newItem.html_url)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewholder {
        return ItemViewholder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        val user = getItem(position)
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener { listener(user) }
    }

    class ItemViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) = itemView.apply {
            tvUsername.text = user.login
            tvUrl.text = user.html_url
            Glide.with(ivAvatar.context)
                .load(user.avatar_url)
                .apply(
                    RequestOptions
                        .circleCropTransform()
                        .override(100, 100)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                )
                .into(ivAvatar)
        }
    }

    fun getUserAtPosition(position: Int): User? {
        return getItem(position)
    }
}

