package com.rtchubs.pharmaerp.ui.order

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.OrderListFragmentBinding
import com.rtchubs.pharmaerp.models.order.SalesData
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class OrderListFragment : BaseFragment<OrderListFragmentBinding, OrderViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_order_list
    override val viewModel: OrderViewModel by viewModels {
        viewModelFactory
    }

    lateinit var orderListAdapter: OrderListAdapter

    override fun onResume() {
        super.onResume()
        orderListAdapter.submitList(orderList)
        visibleGoneEmptyView()
        viewModel.getOrderList(null, 1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerToolbar(viewDataBinding.toolbar)

        orderListAdapter = OrderListAdapter(appExecutors) {
            navigateTo(OrderListFragmentDirections.actionTransactionFragmentToOrderTrackHistoryFragment(it))
        }

        viewDataBinding.orderRecycler.adapter = orderListAdapter

        viewModel.orderItems.observe(viewLifecycleOwner, Observer {
            orderList = it as ArrayList<SalesData>
            orderListAdapter.submitList(orderList)
            visibleGoneEmptyView()
        })

        viewDataBinding.btnCreateOrder.setOnClickListener {
            navigateTo(OrderListFragmentDirections.actionOrderFragmentToCreateOrderFragment())
        }
    }

    private fun visibleGoneEmptyView() {
        if (orderList.isEmpty()) {
            viewDataBinding.orderRecycler.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.orderRecycler.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }

    companion object {
        var orderList = ArrayList<SalesData>()
    }
}