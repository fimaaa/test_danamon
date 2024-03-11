package com.example.admin.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.admin.databinding.ItemUserAdminBinding
import com.general.model.common.user.Member

class UserPagingAdapter(
    private val listener: (Member) -> Unit
) : PagingDataAdapter<Member, UserPagingAdapter.ViewHolder>(ORDER_COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemUserAdminBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class ViewHolder(private val binding: ItemUserAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { it1 -> listener.invoke(it1) }
            }
        }

        fun bind(item: Member) = binding.apply {
            tvUserId.text = item.id
            tvUserName.text = item.username
            tvUserType.text = item.memberType
            tvPhone.text = item.phoneNumber
        }
    }

    companion object {
        private val ORDER_COMPARATOR = object : DiffUtil.ItemCallback<Member>() {
            override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean =
                oldItem == newItem
        }
    }
}