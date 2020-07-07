package com.hendri.githubuser.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hendri.githubuser.R
import com.hendri.githubuser.data.model.User
import kotlinx.android.synthetic.main.item_row_user.view.*

class MainAdapter(
    private val users: ArrayList<User>,
    private val listener: (User) -> Unit
) :
    RecyclerView.Adapter<MainAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.apply {
                tv_item_name.text = user.login
                tv_item_detail.text = user.html_url
                Glide.with(img_item_photo.context)
                    .load(user.avatar_url)
                    .into(img_item_photo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val user = users[position]
        holder.bind(users[position])
        holder.itemView.setOnClickListener{ listener(user) }
    }

    fun addUsers(users: List<User>) {
        this.users.apply {
            clear()
            addAll(users)
        }
    }
}