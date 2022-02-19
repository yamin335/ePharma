package com.rtchubs.pharmaerp.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.MoreBookListItemBinding
import com.rtchubs.pharmaerp.models.*

import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class MoreBookListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((MoreBookItem) -> Unit)? = null

) : DataBoundListAdapter<MoreBookItem, MoreBookListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<MoreBookItem>() {
        override fun areItemsTheSame(oldItem: MoreBookItem, newItem: MoreBookItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: MoreBookItem,
            newItem: MoreBookItem
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun createBinding(parent: ViewGroup): MoreBookListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_more_book, parent, false
        )
    }


    override fun bind(binding: MoreBookListItemBinding, position: Int) {
        val item = getItem(position)
        binding.model = item

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }

        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.logo.setImageResource(R.drawable.engineering_logo)
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }
    }


}