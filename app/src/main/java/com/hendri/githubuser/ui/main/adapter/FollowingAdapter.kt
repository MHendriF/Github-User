package com.hendri.githubuser.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.hendri.githubuser.R
import com.hendri.githubuser.data.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class FollowingAdapter(
    private val users: ArrayList<User>,
    private val listener: (User) -> Unit
) :
    RecyclerView.Adapter<FollowingAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.apply {
                tvUsername.text = user.login
                tvUrl.text = user.html_url
                Glide.with(ivAvatar.context)
                    .load(user.avatar_url)
                    .apply(
                        RequestOptions
                            .circleCropTransform()
                            .override(100, 100)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivAvatar)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val user = users[position]
        holder.bind(users[position])
        holder.itemView.setOnClickListener { listener(user) }
    }

    fun addUsers(users: List<User>) {
        this.users.apply {
            clear()
            addAll(users)
        }
    }
}