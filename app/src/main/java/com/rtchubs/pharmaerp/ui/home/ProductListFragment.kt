package com.rtchubs.pharmaerp.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.ProductListFragmentBinding
import com.rtchubs.pharmaerp.models.OrderProduct
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.shops.ShopDetailsProductListFragment
import com.rtchubs.pharmaerp.util.showSuccessToast
import com.rtchubs.pharmaerp.util.showWarningToast

class ProductListFragment :
    BaseFragment<ProductListFragmentBinding, ProductListViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_product_list
    override val viewModel: ProductListViewModel by viewModels {
        viewModelFactory
    }

    lateinit var productCategoryAdapter: ProductCategoryListAdapter
    lateinit var productListAdapter: ProductListAdapter
    val args: ProductListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        viewModel.toastWarning.observe(viewLifecycleOwner, Observer {
            it?.let { message ->
                showWarningToast(requireContext(), message)
                viewModel.toastWarning.postValue(null)
            }
        })

        viewModel.toastSuccess.observe(viewLifecycleOwner, Observer {
            it?.let { message ->
                showSuccessToast(requireContext(), message)
                viewModel.toastSuccess.postValue(null)
            }
        })

        viewDataBinding.toolbar.title = args.merchant.name

        productCategoryAdapter = ProductCategoryListAdapter(
            appExecutors
        ) { item ->
            //viewDataBinding.imageUrl = item
        }

        viewDataBinding.rvProductCategory.adapter = productCategoryAdapter

        productCategoryAdapter.submitList(listOf("T-Shirt", "Shoes", "Pants", "Hats", "Shorts", "Shocks", "Sunglasses"))

        productListAdapter = ProductListAdapter(
            appExecutors,
            object : ProductListAdapter.ProductListActionCallback {
                override fun addToFavorite(item: PharmaProduct) {
                    //viewModel.addToFavorite(item)
                }

                override fun addToCart(item: PharmaProduct) {
//                    viewModel.addToCart(OrderProduct(item.id, item.name, item.barcode,
//                        item.description, item.buying_price?.toInt(), item.selling_price?.toInt(),
//                        item.mrp?.toInt(), item.expired_date, item.thumbnail,
//                        item.product_image1, item.product_image2,
//                        item.product_image3, item.product_image4,
//                        item.product_image5, item.category_id, item.merchant_id,
//                        item.created_at, item.updated_at,
//                        ShopDetailsProductListFragment.orderMerchant, item.category), 1)
                }

            }) { item ->
            //navController.navigate(ProductListFragmentDirections.actionProductListFragmentToProductDetailsNavGraph(item))
        }

        //viewDataBinding.rvProductList.addItemDecoration(GridRecyclerItemDecorator(2, 40, true))
        viewDataBinding.rvProductList.layoutManager = StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL)
        viewDataBinding.rvProductList.adapter = productListAdapter

        viewModel.productListResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.data?.products?.product?.let { productList ->
                productListAdapter.submitList(productList)
            }
        })

        viewModel.getProductList(1, "", preferencesHelper.getUser().branch_id ?: 0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_product_list, menu)

        val menuItem = menu.findItem(R.id.menu_cart)
        val actionView = menuItem.actionView
        val badge = actionView.findViewById<TextView>(R.id.badge)
        badge.text = viewModel.cartItemCount.value?.toString()
        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }

        viewModel.cartItemCount.observe(viewLifecycleOwner, Observer {
            it?.let { value ->
                if (value < 1) {
                    badge.visibility = View.INVISIBLE
                    return@Observer
                } else {
                    badge.visibility = View.VISIBLE
                    badge.text = value.toString()
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }

            R.id.menu_cart -> {
                navController.navigate(ProductListFragmentDirections.actionProductListFragmentToCartFragment())
            }
        }

        return true
    }
}