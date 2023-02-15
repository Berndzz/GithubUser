package com.hardus.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hardus.githubuser.data.FavoriteRepository
import com.hardus.githubuser.database.entity.FavoriteEntity
import com.hardus.githubuser.databinding.ItemFavoriteUsersBinding

class FavoriteAdapter(
    private val favoriteList: List<FavoriteEntity>,
    private val favoriteRepository: FavoriteRepository
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    class FavoriteViewHolder(var binding: ItemFavoriteUsersBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding =
            ItemFavoriteUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoriteUser = favoriteList[position]
        Glide.with(holder.itemView.context)
            .load(favoriteUser.urlToImage)
            .circleCrop()
            .into(holder.binding.imgItemUsers)
        holder.binding.tvName.text = favoriteUser.username
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(favoriteList[holder.adapterPosition])
        }
        holder.binding.btnDelete.setOnClickListener {
            favoriteRepository.deleteFavorite(favoriteUser)
        }
    }

    override fun getItemCount(): Int = favoriteList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: FavoriteEntity)
    }



}