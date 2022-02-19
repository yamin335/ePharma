package com.rtchubs.pharmaerp.ui.order

import android.annotation.SuppressLint
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
import com.rtchubs.pharmaerp.databinding.OrderProductListItemBinding
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.order.OrderStoreProduct
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class OrderProductListAdapter(
    private val appExecutors: AppExecutors,
    private val cartItemActionCallback: CartItemActionCallback,
    private val itemCallback: ((OrderStoreProduct) -> Unit)? = null
) : DataBoundListAdapter<OrderStoreProduct, OrderProductListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<OrderStoreProduct>() {
        override fun areItemsTheSame(oldItem: OrderStoreProduct, newItem: OrderStoreProduct): Boolean {
            return oldItem.product_id == newItem.product_id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: OrderStoreProduct,
            newItem: OrderStoreProduct
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    interface CartItemActionCallback {
        fun incrementCartItemQuantity(id: Int)
        fun decrementCartItemQuantity(id: Int)
    }

    override fun createBinding(parent: ViewGroup): OrderProductListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_order_product, parent, false
        )
    }


    override fun bind(binding: OrderProductListItemBinding, position: Int) {
        val item = getItem(position)
        binding.item = item
        //binding.imageUrl = item.thumbnail
        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.thumbnail.setImageResource(R.drawable.image_placeholder)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }

        binding.remove.setOnClickListener {
            itemCallback?.invoke(item)
        }

        binding.incrementQuantity.setOnClickListener {
            cartItemActionCallback.incrementCartItemQuantity(item.product_id)
        }
        binding.decrementQuantity.setOnClickListener {
            if (item.available_qty ?: 0 > 1) {
                cartItemActionCallback.decrementCartItemQuantity(item.product_id)
            }
        }
    }
}