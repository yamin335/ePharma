package com.rtchubs.pharmaerp.ui.gift_point

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.GiftPointHistoryDetailsListItemBinding
import com.rtchubs.pharmaerp.models.GiftPointsHistoryDetailsRewards
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class GiftPointsHistoryDetailsListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((GiftPointsHistoryDetailsRewards) -> Unit)? = null
) : DataBoundListAdapter<GiftPointsHistoryDetailsRewards, GiftPointHistoryDetailsListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<GiftPointsHistoryDetailsRewards>() {
        override fun areItemsTheSame(oldItem: GiftPointsHistoryDetailsRewards, newItem: GiftPointsHistoryDetailsRewards): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: GiftPointsHistoryDetailsRewards,
            newItem: GiftPointsHistoryDetailsRewards
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): GiftPointHistoryDetailsListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_gift_point_history_details, parent, false
        )
    }


    override fun bind(binding: GiftPointHistoryDetailsListItemBinding, position: Int) {
        val item = getItem(position)
        binding.title = item.remarks ?: "Reason not found"
        binding.description = if (item.is_approved == 1) "Status: Approved" else "Status: Not Approved"
        binding.point = item.reward?.toString() ?: "0"

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }
    }
}