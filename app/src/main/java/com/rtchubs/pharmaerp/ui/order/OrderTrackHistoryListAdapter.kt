package com.rtchubs.pharmaerp.ui.order

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.OrderTrackHistoryListItemBinding
import com.rtchubs.pharmaerp.models.order.OrderTrackHistory
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class OrderTrackHistoryListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((OrderTrackHistory) -> Unit)? = null
) : DataBoundListAdapter<OrderTrackHistory, OrderTrackHistoryListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<OrderTrackHistory>() {
        override fun areItemsTheSame(oldItem: OrderTrackHistory, newItem: OrderTrackHistory): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: OrderTrackHistory,
            newItem: OrderTrackHistory
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): OrderTrackHistoryListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_order_track, parent, false
        )
    }


    override fun bind(binding: OrderTrackHistoryListItemBinding, position: Int) {
        val item = getItem(position)
        binding.item = item
        val context = binding.root.context

        if (item.isActive) {
            binding.circleCard.setCardBackgroundColor(ContextCompat.getColorStateList(context, R.color.teal))
            binding.circleCard.strokeColor = context.getColor(R.color.teal)
            binding.markIcon.imageTintList = ContextCompat.getColorStateList(context, R.color.white)
        } else {
            binding.circleCard.setCardBackgroundColor(ContextCompat.getColorStateList(context, R.color.white))
            binding.circleCard.strokeColor = context.getColor(R.color.colorLightGray)
            binding.markIcon.imageTintList = ContextCompat.getColorStateList(context, R.color.colorLightGray)
        }

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }
    }
}