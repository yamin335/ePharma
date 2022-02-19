package com.rtchubs.pharmaerp.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rtchubs.pharmaerp.AppExecutors
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.ProductListItemBinding

import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.util.DataBoundListAdapter
import kotlinx.android.synthetic.main.popup_menu_product_item.view.*

class ProductListAdapter(
    private val appExecutors: AppExecutors,
    private val itemActionCallback: ProductListActionCallback,
    private val itemCallback: ((PharmaProduct) -> Unit)? = null
) : DataBoundListAdapter<PharmaProduct, ProductListItemBinding>(
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

    }) {
    // Properties
    private val viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    val onClicked = MutableLiveData<PharmaProduct>()
    override fun createBinding(parent: ViewGroup): ProductListItemBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_product, parent, false
        )
    }


    override fun bind(binding: ProductListItemBinding, position: Int) {
        val context = binding.root.context
        val item = getItem(position)
        binding.productName = item.name
        //binding.imageUrl = item.thumbnail
        binding.productPrice = "$ ${item.mrp}"

        binding.root.setOnClickListener {
            itemCallback?.invoke(item)
        }

        binding.menu.setOnClickListener {
            Toast.makeText(binding.root.context, "PopUp Menu Working", Toast.LENGTH_LONG).show()
        }

        binding.imageRequestListener = object: RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                binding.logo.setImageResource(R.drawable.image_placeholder)
                return true
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                return false
            }
        }

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val viewPopupMenu = inflater.inflate(R.layout.popup_menu_product_item, null)
        val popupMenu = PopupWindow(viewPopupMenu, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupMenu.isOutsideTouchable = true
        //popupMenu.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        popupMenu.animationStyle = android.R.style.Animation_Dialog
        popupMenu.elevation = 20F

        popupMenu.setOnDismissListener {
            //        Toast.makeText(context, "Dismissed!!!", Toast.LENGTH_LONG).show()
        }

        val popupMenuView = popupMenu.contentView
        popupMenuView.menuFavorite.setOnClickListener {
            itemActionCallback.addToFavorite(item)
            popupMenu.dismiss()
        }

        popupMenuView.menuCart.setOnClickListener {
            itemActionCallback.addToCart(item)
            popupMenu.dismiss()
        }

        binding.menu.setOnClickListener {
            popupMenu.showAsDropDown(binding.menu,-200, -150, Gravity.NO_GRAVITY)
        }
    }

    interface ProductListActionCallback {
        fun addToFavorite(item: PharmaProduct)
        fun addToCart(item: PharmaProduct)
    }
}