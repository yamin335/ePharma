package com.rtchubs.pharmaerp.ui.purchase_list

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.api.ApiCallStatus
import com.rtchubs.pharmaerp.databinding.PurchaseListFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductPurchaseFragment : BaseFragment<PurchaseListFragmentBinding, PurchaseListFragmentViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_purchase_list
    override val viewModel: PurchaseListFragmentViewModel by viewModels {
        viewModelFactory
    }

    lateinit var productPurchaseListAdapter: ProductPurchaseListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        productPurchaseListAdapter = ProductPurchaseListAdapter {
            navigateTo(ProductPurchaseFragmentDirections.actionProductPurchaseFragmentToReceiveProductNav(product = null, isEdit = false, purchase = it))
        }

        viewDataBinding.purchaseListRecycler.adapter = productPurchaseListAdapter

        viewModel.purchaseList.observe(viewLifecycleOwner, Observer { purchases ->
            if (!purchases.isNullOrEmpty()) {
                viewModel.apiCallStatus.postValue(ApiCallStatus.LOADING)
                productPurchaseListAdapter.clearData()
                CoroutineScope(Dispatchers.Main.immediate).launch {
                    delay(1000)
                    purchases.forEachIndexed { index, purchase ->
                        productPurchaseListAdapter.addItemToList(purchase, index)
                    }
                    viewModel.apiCallStatus.postValue(ApiCallStatus.SUCCESS)
                }
            }
            visibleGoneEmptyView()
        })

        if (viewModel.purchaseList.value.isNullOrEmpty()) {
            viewModel.getPurchaseList(1,"")
        }
    }

    private fun visibleGoneEmptyView() {
        if (viewModel.purchaseList.value.isNullOrEmpty()) {
            viewDataBinding.purchaseListRecycler.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.purchaseListRecycler.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }
}