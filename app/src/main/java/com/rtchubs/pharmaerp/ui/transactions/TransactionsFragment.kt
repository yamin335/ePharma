package com.rtchubs.pharmaerp.ui.transactions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.databinding.TransactionsFragmentBinding
import com.rtchubs.pharmaerp.models.order.SalesData
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class TransactionsFragment : BaseFragment<TransactionsFragmentBinding, TransactionsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_transactions
    override val viewModel: TransactionsViewModel by viewModels {
        viewModelFactory
    }

    lateinit var transactionsListAdapter: TransactionsListAdapter

    override fun onResume() {
        super.onResume()
        transactionsListAdapter.submitList(orderList)
        viewModel.getOrderList(null, 1)
        visibleGoneEmptyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        transactionsListAdapter = TransactionsListAdapter(appExecutors) {
            navigateTo(TransactionsFragmentDirections.actionTransactionsFragmentToTransactionDetailsFragment(it))
        }

        viewDataBinding.transactionsRecycler.adapter = transactionsListAdapter

        viewModel.orderItems.observe(viewLifecycleOwner, Observer {
            orderList = it as ArrayList<SalesData>
            transactionsListAdapter.submitList(orderList)
            visibleGoneEmptyView()
        })
    }

    private fun visibleGoneEmptyView() {
        if (orderList.isEmpty()) {
            viewDataBinding.container.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.container.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }

    companion object {
        var orderList = ArrayList<SalesData>()
    }
}