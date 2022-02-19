package com.rtchubs.pharmaerp.ui.mpos

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
import com.rtchubs.pharmaerp.databinding.MPOSOrderProductListItemBinding
import com.rtchubs.pharmaerp.models.MPOSOrderProduct
import com.rtchubs.pharmaerp.models.order.OrderStoreProduct
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class MPOSOrderProductListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((OrderStoreProduct) -> Unit)? = null
) : DataBoundListAdapter<OrderStoreProduct, MPOSOrderProductListItemBinding>(
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

    override fun createBinding(parent: ViewGroup): MPOSOrderProductListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_mpos_order_product, parent, false
        )
    }


    override fun bind(binding: MPOSOrderProductListItemBinding, position: Int) {
        val item = getItem(position)
        binding.productName = item.product_name
        binding.productDescription = item.description
        binding.productPrice = item.price?.toString()
        //binding.imageUrl = item.product?.thumbnail
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
    }
}