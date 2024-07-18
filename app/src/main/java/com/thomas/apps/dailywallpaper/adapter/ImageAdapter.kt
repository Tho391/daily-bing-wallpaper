package com.thomas.apps.dailywallpaper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.ImageRequest
import com.thomas.apps.dailywallpaper.R
import com.thomas.apps.dailywallpaper.databinding.ItemBingImageBinding
import com.thomas.apps.dailywallpaper.utils.OnClickListener
import timber.log.Timber

class ImageAdapter :
    PagingDataAdapter<ImageAdapter.ImageItem, ImageAdapter.ViewHolder>(ImageItemDC()) {

    var onDownloadClick: OnClickListener<ImageItem>? = null
    var onSetWallpaperClick: OnClickListener<ImageItem>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder.from(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        getItem(position)?.let {
            holder.onDownloadClick = onDownloadClick
            holder.onSetWallpaperClick = onSetWallpaperClick
            holder.bind(it)
        }
    }

    class ViewHolder private constructor(private val binding: ItemBingImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var onDownloadClick: OnClickListener<ImageItem>? = null
        var onSetWallpaperClick: OnClickListener<ImageItem>? = null

        fun bind(item: ImageItem) {
            binding.item = item

            if (item.imageUrl.isEmpty()) {
                binding.imageView.load(R.drawable.default_image) {
                    listener(
                        onError = { request: ImageRequest, throwable: Throwable ->
                            Timber.e("url: ${item.imageUrl}")
                            Timber.e(throwable)
                        }
                    )
                }
            } else {
                binding.imageView.load(item.imageUrl) {
                    listener(
                        onError = { request: ImageRequest, throwable: Throwable ->
                            Timber.e("url: ${item.imageUrl}")
                            Timber.e(throwable)
                        }
                    )
                }
            }


            binding.buttonDownload.setOnClickListener {
                onDownloadClick?.invoke(item)
            }

            binding.buttonSetWallpaper.setOnClickListener {
                onSetWallpaperClick?.invoke(item)
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemBingImageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    private class ImageItemDC : DiffUtil.ItemCallback<ImageItem>() {
        override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
            return oldItem == newItem
        }
    }

    data class ImageItem(
        val id: String,
        val date: String,
        val title: String,
        val copyright: String,
        val imageUrl: String,
    ) {
        fun getDateString(): String {
            return date
        }
    }


}