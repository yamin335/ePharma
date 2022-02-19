package com.rtchubs.pharmaerp.ui.stock_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.StockProductListItemBinding
import com.rtchubs.pharmaerp.models.product_stock.StockProductWithDetails

class StockProductsListAdapter constructor(private val callback: (StockProductWithDetails) -> Unit): RecyclerView.Adapter<StockProductsListAdapter.ViewHolder>() {

    private var productsList: ArrayList<StockProductWithDetails> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: StockProductListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_stock_product, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productsList[position])
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun addItemToList(item: StockProductWithDetails, position: Int) {
        this.productsList.add(item)
        notifyItemInserted(position)
    }

    fun submitList(productsList: List<StockProductWithDetails>) {
        this.productsList = productsList as ArrayList<StockProductWithDetails>
        notifyDataSetChanged()
    }

    fun clearData() {
        this.productsList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding: StockProductListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: StockProductWithDetails) {
            binding.productName = item.product?.name

            if (!item.details.isNullOrEmpty()) {
                binding.lotNumber = item.details.first().lot
                binding.quantity = item.details.first().qty?.toString()
                binding.inStock = item.details.first().qty?.toString()
                binding.receivedDate = item.details.first().created_at
            }

            binding.expandableLayout.isExpanded = item.isExpanded

            binding.topBar.setOnClickListener {
                toggleExpanded(item, binding)
                binding.expandableLayout.isExpanded = item.isExpanded
            }

            binding.btnDetails.setOnClickListener {
                callback(item)
            }

            binding.executePendingBindings()
        }

        fun toggleExpanded(item: StockProductWithDetails, binding: StockProductListItemBinding) {
            item.isExpanded = !item.isExpanded
            if (item.isExpanded) {
                binding.arrowIndicator.animate().setDuration(200).rotation(180F)
            } else {
                binding.arrowIndicator.animate().setDuration(200).rotation(0F)
            }
        }
    }
}
