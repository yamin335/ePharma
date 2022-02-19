package com.rtchubs.pharmaerp.ui.add_product

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.SampleImageListItemBinding

class SampleImageListAdapter constructor(
    private val itemCallback: (Int) -> Unit
): RecyclerView.Adapter<SampleImageListAdapter.ViewHolder>() {
    private var bitmapList: HashMap<Int, Bitmap?> = HashMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: SampleImageListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_add_product_sample_image, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bitmapList[position])
    }

    override fun getItemCount(): Int {
        return bitmapList.size
    }

    fun setImage(image: Bitmap?, position: Int) {
        bitmapList[position] = image
        notifyDataSetChanged()
    }

    fun getImageList(): List<Bitmap?> {
        val imageList: ArrayList<Bitmap?> = arrayListOf(null, null, null, null, null)
        for (key in bitmapList.keys) {
            imageList[key] = bitmapList[key]
        }
        return imageList
    }

    inner class ViewHolder (private val binding: SampleImageListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Bitmap?) {
            if (item == null) {
                binding.llAddImage.visibility = View.VISIBLE
                binding.remove.visibility = View.GONE
                binding.image.setImageBitmap(item)
            } else {
                Glide.with(binding.root.context)
                    .load(item)
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(
                        binding.root.context,
                        R.drawable.image_placeholder
                    ))
                    .into(binding.image)

                binding.llAddImage.visibility = View.GONE
                binding.remove.visibility = View.VISIBLE
            }

            binding.remove.setOnClickListener {
                bitmapList[absoluteAdapterPosition] = null
                notifyDataSetChanged()
            }

            binding.llAddImage.setOnClickListener {
                itemCallback(absoluteAdapterPosition)
            }

            binding.image.setOnClickListener {
                itemCallback(absoluteAdapterPosition)
            }
            binding.executePendingBindings()
        }
    }
}
