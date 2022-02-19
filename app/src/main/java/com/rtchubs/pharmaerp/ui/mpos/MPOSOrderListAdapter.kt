package com.rtchubs.pharmaerp.ui.mpos

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.MPOSOrderListItemBinding
import com.rtchubs.pharmaerp.models.order.SalesData
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class MPOSOrderListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((SalesData) -> Unit)? = null
) : DataBoundListAdapter<SalesData, MPOSOrderListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<SalesData>() {
        override fun areItemsTheSame(oldItem: SalesData, newItem: SalesData): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: SalesData,
            newItem: SalesData
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): MPOSOrderListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_mpos_orders, parent, false
        )
    }


    override fun bind(binding: MPOSOrderListItemBinding, position: Int) {
        val item = getItem(position)
        binding.invoice = item.OurReference ?: "Undefined Invoice"
        binding.date = item.date ?: "No date found"
        binding.subTotal = "${binding.root.context.getString(R.string.sign_taka)} ${item.grand_total}"

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }
    }
}