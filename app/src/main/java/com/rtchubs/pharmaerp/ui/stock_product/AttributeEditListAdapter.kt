package com.rtchubs.pharmaerp.ui.stock_product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.AttributeEditListItemBinding
import com.rtchubs.pharmaerp.models.Attribute

class AttributeEditListAdapter: RecyclerView.Adapter<AttributeEditListAdapter.ViewHolder>() {

    private var attributeList: ArrayList<Attribute> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: AttributeEditListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_edit_product_attributes, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(attributeList[position])
    }

    override fun getItemCount(): Int {
        return attributeList.size
    }

    fun getItemList(): List<Attribute> {
        return attributeList
    }

    fun submitList(attributeList: List<Attribute>) {
        this.attributeList = attributeList as ArrayList<Attribute>
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding: AttributeEditListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Attribute) {
            binding.attribute = item
            binding.executePendingBindings()
        }
    }
}
