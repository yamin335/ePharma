package com.rtchubs.pharmaerp.ui.add_product

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.ProductDetailsSampleImageListItemBinding

class ProductDetailsSampleImageListAdapter: RecyclerView.Adapter<ProductDetailsSampleImageListAdapter.ViewHolder>() {
    private var bitmapList: List<Bitmap> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ProductDetailsSampleImageListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_product_sample_image, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bitmapList[position])
    }

    override fun getItemCount(): Int {
        return bitmapList.size
    }

    fun submitImageList(imageList: List<Bitmap>) {
        bitmapList = imageList
        notifyDataSetChanged()
    }

    inner class ViewHolder (private val binding: ProductDetailsSampleImageListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bitmap) {
            binding.image.setImageBitmap(item)
            binding.executePendingBindings()
        }
    }
}
