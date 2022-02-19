package com.rtchubs.pharmaerp.ui.stock_product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.StockProductDetailsFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class StockProductsDetailsFragment : BaseFragment<StockProductDetailsFragmentBinding, StockProductDetailsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_stock_products_details
    override val viewModel: StockProductDetailsViewModel by viewModels {
        viewModelFactory
    }

    lateinit var stockProductDetailsListAdapter: StockProductDetailsListAdapter

    val args: StockProductsDetailsFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        viewModel.getStockProductDetails(args.productDetailId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        viewDataBinding.toolbar.title = args.title

        stockProductDetailsListAdapter = StockProductDetailsListAdapter(appExecutors) {
            //navigateTo(OrderListFragmentDirections.actionTransactionFragmentToOrderTrackHistoryFragment(it))
        }

        viewDataBinding.productRecycler.adapter = stockProductDetailsListAdapter

        viewModel.stockProductsList.observe(viewLifecycleOwner, Observer { productList ->
            if (!productList.isNullOrEmpty()) {
                stockProductDetailsListAdapter.submitList(productList)
            }
            visibleGoneEmptyView()
        })

        viewDataBinding.btnPrint.setOnClickListener {
//            navigateTo(StockProductsDetailsFragmentDirections.actionStockProductsDetailsFragmentToBarcodePrintNav(
//                "https://engrsapps.s3.ap-southeast-1.amazonaws.com/fbk/f-sc/f-ssc/f-ssc-sci/f-ssc-sci-ex1/f-ssc-sci-ex1-c/%E0%A7%A7%E0%A6%AE+%E0%A6%85%E0%A6%A7%E0%A7%8D%E0%A6%AF%E0%A6%BE%E0%A7%9F.pdf"))
            navigateTo(StockProductsDetailsFragmentDirections.actionStockProductsDetailsFragmentToBarcodePrintNav("https://backend.mobmalls.com/api/print?product_detail_id=${args.productDetailId}"))
        }
    }

    private fun visibleGoneEmptyView() {
        if (viewModel.stockProductsList.value.isNullOrEmpty()) {
            viewDataBinding.productRecycler.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.productRecycler.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }
}