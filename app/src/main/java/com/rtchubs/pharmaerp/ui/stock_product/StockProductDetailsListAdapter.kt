package com.rtchubs.pharmaerp.ui.stock_product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.StockProductDetailsListItemBinding
import com.rtchubs.pharmaerp.models.product_stock.StockProductsDetails
import com.rtchubs.pharmaerp.util.DataBoundListAdapter

class StockProductDetailsListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((StockProductsDetails) -> Unit)? = null
) : DataBoundListAdapter<StockProductsDetails, StockProductDetailsListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<StockProductsDetails>() {
        override fun areItemsTheSame(oldItem: StockProductsDetails, newItem: StockProductsDetails): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: StockProductsDetails,
            newItem: StockProductsDetails
        ): Boolean {
            return oldItem == newItem
        }

    }) {

    override fun createBinding(parent: ViewGroup): StockProductDetailsListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_stock_product_details, parent, false
        )
    }


    override fun bind(binding: StockProductDetailsListItemBinding, position: Int) {
        val context = binding.root.context
        val item = getItem(position)
        binding.barcode = item.barcode ?: "N/A"
        binding.buyingPrice = "${item.buying_price?.toString() ?: "0"} ${context.getString(R.string.sign_taka)}"
        binding.sellingPrice = "${item.selling_price?.toString() ?: "0"} ${context.getString(R.string.sign_taka)}"

        binding.btnChange.setOnClickListener {
            itemCallback?.invoke(item)
        }
    }
}