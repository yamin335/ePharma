package com.rtchubs.pharmaerp.ui.gift_point

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.GiftPointRequestListItemBinding
import com.rtchubs.pharmaerp.models.GiftPointRequest
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class GiftPointRequestListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((GiftPointRequest) -> Unit)? = null
) : DataBoundListAdapter<GiftPointRequest, GiftPointRequestListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<GiftPointRequest>() {
        override fun areItemsTheSame(oldItem: GiftPointRequest, newItem: GiftPointRequest): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: GiftPointRequest,
            newItem: GiftPointRequest
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): GiftPointRequestListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_gift_point_request, parent, false
        )
    }


    override fun bind(binding: GiftPointRequestListItemBinding, position: Int) {
        val item = getItem(position)
        binding.customerName = item.name ?: "Unknown Customer"
        binding.giftPoint = item.reward?.toString() ?: "0"

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }
    }
}