package com.rtchubs.pharmaerp.ui.add_product

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.AddProductListItemBinding
import com.rtchubs.pharmaerp.models.customers.Customer
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.util.DataBoundListAdapter
import java.util.*
import kotlin.collections.ArrayList

class AllProductListAdapter(
    private val appExecutors: AppExecutors,
    private val itemCallback: ((PharmaProduct) -> Unit)? = null
) : DataBoundListAdapter<PharmaProduct, AddProductListItemBinding>(
    appExecutors = appExecutors, diffCallback = object : DiffUtil.ItemCallback<PharmaProduct>() {
        override fun areItemsTheSame(oldItem: PharmaProduct, newItem: PharmaProduct): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: PharmaProduct,
            newItem: PharmaProduct
        ): Boolean {
            return oldItem == newItem
        }

    }), Filterable {

    private var dataList: ArrayList<PharmaProduct> = ArrayList()
    private var filteredDataList: ArrayList<PharmaProduct> = ArrayList()

    override fun createBinding(parent: ViewGroup): AddProductListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_add_product, parent, false
        )
    }

    fun submitDataList(dataList: List<PharmaProduct>) {
        this.dataList = dataList as ArrayList<PharmaProduct>
        this.filteredDataList = this.dataList
        submitList(this.filteredDataList)
    }

    override fun getItemCount(): Int {
        return filteredDataList.size
    }

    override fun bind(binding: AddProductListItemBinding, position: Int) {
        val item = getItem(position)
        binding.item = item
        //binding.imageUrl = item.thumbnail
        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.thumbnail.setImageResource(R.drawable.image_placeholder)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                var filteredList: MutableList<PharmaProduct> = ArrayList()
                if (charString.isEmpty()) {
                    filteredList = dataList
                } else {
                    for (row in dataList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.name?.toLowerCase(Locale.ROOT)?.contains(charSequence) == true) {
                            filteredList.add(row)
                        }
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filterResults.values?.let {
                    filteredDataList = (it as? ArrayList<*>)?.filterIsInstance<PharmaProduct>() as ArrayList<PharmaProduct>? ?: ArrayList()
                    submitList(filteredDataList)
                }
            }
        }
    }
}