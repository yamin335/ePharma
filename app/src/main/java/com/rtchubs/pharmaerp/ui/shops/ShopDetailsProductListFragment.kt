package com.rtchubs.pharmaerp.ui.shops

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.ShopDetailsProductListFragmentBinding
import com.rtchubs.pharmaerp.models.Merchant
import com.rtchubs.pharmaerp.models.OrderMerchant
import com.rtchubs.pharmaerp.models.OrderProduct
import com.rtchubs.pharmaerp.models.Product
import com.rtchubs.pharmaerp.models.products.PharmaProduct
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.home.*
import com.rtchubs.pharmaerp.util.showSuccessToast
import com.rtchubs.pharmaerp.util.showWarningToast

private const val MERCHANT = "merchant"

class ShopDetailsProductListFragment :
    BaseFragment<ShopDetailsProductListFragmentBinding, ProductListViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_shop_details_product_list
    override val viewModel: ProductListViewModel by viewModels {
        viewModelFactory
    }

    private var merchant: Merchant? = null

    lateinit var productListAdapter: ProductListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            merchant = it.getSerializable(MERCHANT) as Merchant
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        orderMerchant = OrderMerchant(merchant?.id, merchant?.name, merchant?.user_name,
            merchant?.password, merchant?.shop_name, merchant?.mobile,
            merchant?.lat, merchant?.long, merchant?.whatsApp,
            merchant?.email, merchant?.address, merchant?.shop_address,
            merchant?.shop_logo, merchant?.thumbnail, merchant?.isActive,
            merchant?.shopping_mall_id, merchant?.shopping_mall_level_id,
            merchant?.created_at, merchant?.updated_at)

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

        productListAdapter = ProductListAdapter(
            appExecutors,
            object : ProductListAdapter.ProductListActionCallback {
                override fun addToFavorite(item: PharmaProduct) {
                    //viewModel.addToFavorite(item)
                }

                override fun addToCart(item: PharmaProduct) {
//                    viewModel.addToCart(
//                        OrderProduct(item.id, item.name, item.barcode,
//                        item.description, item.buying_price?.toInt(), item.selling_price?.toInt(),
//                        item.mrp?.toInt(), item.expired_date, item.thumbnail,
//                        item.product_image1, item.product_image2,
//                        item.product_image3, item.product_image4,
//                        item.product_image5, item.category_id, item.merchant_id,
//                        item.created_at, item.updated_at,
//                        orderMerchant, item.category), 1)
                }

            }) { item ->
            setFragmentResult("goToProductDetails", bundleOf("product" to item))
        }

        viewDataBinding.rvProductList.layoutManager = StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL)
        viewDataBinding.rvProductList.adapter = productListAdapter

        viewModel.productListResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.data?.let { productList ->
//                if (productList.size < 7) {
//                    productListAdapter.submitList(productList)
//                } else {
//                    productList.subList(0, 6)
//                }
            }
        })

        viewModel.getProductList(1, "", preferencesHelper.getUser().branch_id ?: 0)

        viewDataBinding.moreProduct.setOnClickListener {
            merchant?.let { merchant ->
                setFragmentResult("fromProductList", bundleOf("merchant" to merchant))
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param merchant Selected Merchant.
         * @return A new instance of fragment 'ShopDetailsProductListFragment'.
         */

        var orderMerchant: OrderMerchant? = null

        @JvmStatic
        fun newInstance(merchant: Merchant) =
            ShopDetailsProductListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(MERCHANT, merchant)
                }
            }
    }
}