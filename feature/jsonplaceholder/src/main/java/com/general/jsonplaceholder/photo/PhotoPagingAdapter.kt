package com.general.jsonplaceholder.photo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.general.jsonplaceholder.R
import com.general.jsonplaceholder.databinding.ItemJsonPhotoBinding
import com.general.model.jsonplaceholder.Photo
import com.general.common.R as commonR

class PhotoPagingAdapter(
    private val listener: (Photo) -> Unit
) : PagingDataAdapter<Photo, PhotoPagingAdapter.ViewHolder>(ORDER_COMPARATOR) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemJsonPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    inner class ViewHolder(private val binding: ItemJsonPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { it1 -> listener.invoke(it1) }
            }
        }

        fun bind(item: Photo) = binding.apply {
            tvPhotoId.text = item.id.toString()
            tvPhotoAlbumId.text = root.context.getString(R.string.albumid, item.albumId.toString())
            tvPhotoTitle.text = item.title
            Glide.with(root.context)
                .load(item.thumbnailUrl)
                .placeholder(ContextCompat.getDrawable(root.context, commonR.color.colorOptionalSecondary))
                .into(tvPhotoImage)
        }
    }

    companion object {
        private val ORDER_COMPARATOR = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean =
                oldItem == newItem
        }
    }
}