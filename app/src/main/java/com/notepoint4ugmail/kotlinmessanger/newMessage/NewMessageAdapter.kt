package com.notepoint4ugmail.kotlinmessanger.newMessage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.notepoint4ugmail.kotlinmessanger.databinding.SingleUserLayoutBinding
import com.notepoint4ugmail.kotlinmessanger.model.User

class NewMessageAdapter(val userClickListener: NewUserClickListener):ListAdapter<User,NewMessageAdapter.UserListHolder>(UserListDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListHolder {
        return UserListHolder.from(parent)
    }

    override fun onBindViewHolder(holder: UserListHolder, position: Int) {
        val userItem = getItem(position)

        holder.itemView.setOnClickListener {
            userClickListener.onClick(userItem)
        }

        holder.bind(userItem)
    }



    class UserListHolder(private val binding:SingleUserLayoutBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(userItem: User?) {
            binding.userData = userItem
            binding.executePendingBindings()

            binding.userEmailSingleCell.text = userItem?.userName

            Glide.with(binding.userProfileImageSingleCell)
                .load(userItem?.profileImageUrl)
                .into(binding.userProfileImageSingleCell)

        }

        companion object{
            fun from(parent: ViewGroup): UserListHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = SingleUserLayoutBinding.inflate(inflater)
                return UserListHolder(binding)
            }
        }
    }


    class UserListDiffUtil:DiffUtil.ItemCallback<User>(){
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }
    }


    class NewUserClickListener(private val onCLickListener:(user: User)->Unit){
        fun onClick(userItem: User) = onCLickListener(userItem)
    }
}