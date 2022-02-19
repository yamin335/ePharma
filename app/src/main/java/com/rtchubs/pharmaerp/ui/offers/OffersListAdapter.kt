package com.rtchubs.pharmaerp.ui.offers

import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.OfferListItemBinding
import com.rtchubs.pharmaerp.models.OfferItem
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class OffersListAdapter(
    private val appExecutors: AppExecutors,
    private val itemSelectionCallback: ((OfferItem) -> Unit)
) : DataBoundListAdapter<OfferItem, OfferListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<OfferItem>() {
        override fun areItemsTheSame(oldItem: OfferItem, newItem: OfferItem): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: OfferItem,
            newItem: OfferItem
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): OfferListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_offer, parent, false
        )
    }

    override fun bind(binding: OfferListItemBinding, position: Int) {
        val item = getItem(position)
        binding.item = item

        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.thumbnail.setImageResource(R.drawable.image_placeholder)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }

        binding.root.setOnClickListener {
            itemSelectionCallback(item)
        }

        binding.mrp.apply {
            paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            text = "${binding.root.context.getString(R.string.sign_taka)}120"
        }
    }
}