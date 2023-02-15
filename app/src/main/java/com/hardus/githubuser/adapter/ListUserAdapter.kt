package com.hardus.githubuser.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hardus.githubuser.databinding.ItemRowUsersBinding
import com.hardus.githubuser.respons.ItemsItem

class ListUserAdapter(
    private val listUsers: List<ItemsItem>
) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemRowUsersBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, i: Int): ListViewHolder {
        val binding =
            ItemRowUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {


        val users = listUsers[position]
        Glide.with(holder.itemView.context)
            .load(users.avatarUrl)
            .circleCrop()
            .into(holder.binding.imgItemUsers)
        holder.binding.tvName.text = users.login
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUsers[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int = listUsers.size

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}



