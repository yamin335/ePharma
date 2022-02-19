package com.rtchubs.pharmaerp.ui.purchase_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.PurchaseListItemBinding
import com.rtchubs.pharmaerp.models.purchase_list.PurchaseListResponseData

class ProductPurchaseListAdapter constructor(private val callback: (PurchaseListResponseData) -> Unit): RecyclerView.Adapter<ProductPurchaseListAdapter.ViewHolder>() {

    private var purchaseList: ArrayList<PurchaseListResponseData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: PurchaseListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_purchase_list, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(purchaseList[position])
    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }

    fun addItemToList(item: PurchaseListResponseData, position: Int) {
        this.purchaseList.add(item)
        notifyItemInserted(position)
    }

    fun submitList(purchaseList: List<PurchaseListResponseData>) {
        this.purchaseList = purchaseList as ArrayList<PurchaseListResponseData>
        notifyDataSetChanged()
    }

    fun clearData() {
        this.purchaseList.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding: PurchaseListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PurchaseListResponseData) {
            binding.purchase = item

            binding.expandableLayout.isExpanded = item.isExpanded

            binding.topBar.setOnClickListener {
                toggleExpanded(item, binding)
                binding.expandableLayout.isExpanded = item.isExpanded
            }

            binding.btnReceiveProduct.setOnClickListener {
                callback(item)
            }

            binding.executePendingBindings()
        }

        fun toggleExpanded(item: PurchaseListResponseData, binding: PurchaseListItemBinding) {
            item.isExpanded = !item.isExpanded
            if (item.isExpanded) {
                binding.arrowIndicator.animate().setDuration(200).rotation(180F)
            } else {
                binding.arrowIndicator.animate().setDuration(200).rotation(0F)
            }
        }
    }
}
