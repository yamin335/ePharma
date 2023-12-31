package com.rtchubs.pharmaerp.ui.home

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.FavoriteListItemBinding

import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.util.DataBoundListAdapter
import kotlinx.android.synthetic.main.popup_menu_product_item.view.*

class FavoriteListAdapter(
    private val appExecutors: AppExecutors,
    private val actionCallback: FavoriteListActionCallback,
    private val itemCallback: ((Product) -> Unit)? = null
) : DataBoundListAdapter<Product, FavoriteListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Product,
            newItem: Product
        ): Boolean {
            return oldItem == newItem
        }

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    val onClicked = MutableLiveData<Product>()
    override fun createBinding(parent: ViewGroup): FavoriteListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_favorite, parent, false
        )
    }


    override fun bind(binding: FavoriteListItemBinding, position: Int) {
        val item = getItem(position)
        binding.item = item
        binding.imageUrl = item.thumbnail

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }

        binding.remove.setOnClickListener {
            actionCallback.onRemove(item)
        }

        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.thumbnail.setImageResource(R.drawable.image_placeholder)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }
    }

    interface FavoriteListActionCallback {
        fun onRemove(item: Product)
    }
}