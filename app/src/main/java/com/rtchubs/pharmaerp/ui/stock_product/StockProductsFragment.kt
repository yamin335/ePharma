package com.rtchubs.pharmaerp.ui.stock_product

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.api.ApiCallStatus
import com.rtchubs.pharmaerp.databinding.StockProductFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StockProductsFragment : BaseFragment<StockProductFragmentBinding, StockProductsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_stock_products
    override val viewModel: StockProductsViewModel by viewModels {
        viewModelFactory
    }

    lateinit var stockProductsListAdapter: StockProductsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        stockProductsListAdapter = StockProductsListAdapter {
            if (!it.details.isNullOrEmpty()) {
                it.details.first().id?.let { id ->
                    navigateTo(StockProductsFragmentDirections.actionStockProductsFragmentToStockProductsDetailsFragment(
                        it.details.first().lot ?: "Unknown Lot", id))
                }
            }
        }

        viewDataBinding.productRecycler.adapter = stockProductsListAdapter

        viewModel.stockProductsList.observe(viewLifecycleOwner, Observer { productList ->
            if (!productList.isNullOrEmpty()) {
                viewModel.apiCallStatus.postValue(ApiCallStatus.LOADING)
                stockProductsListAdapter.clearData()
                CoroutineScope(Dispatchers.Main.immediate).launch {
                    delay(1000)
                    productList.forEachIndexed { index, stockProductWithDetails ->
                        stockProductsListAdapter.addItemToList(stockProductWithDetails, index)
                    }
                    viewModel.apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                }
            }
            visibleGoneEmptyView()
        })

        viewDataBinding.btnReceiveProduct.setOnClickListener {
            navigateTo(StockProductsFragmentDirections.actionStockProductsFragmentToReceiveProductNav(product = null, isEdit = false, purchase = null))
        }

        if (viewModel.stockProductsList.value.isNullOrEmpty()) {
            viewModel.getStockProduct("")
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