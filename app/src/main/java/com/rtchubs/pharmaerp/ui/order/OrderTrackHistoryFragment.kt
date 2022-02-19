package com.rtchubs.pharmaerp.ui.order

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.OrderTrackHistoryFragmentBinding
import com.rtchubs.pharmaerp.models.order.OrderTrackHistory
import com.rtchubs.pharmaerp.models.order.SalesData
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class OrderTrackHistoryFragment : BaseFragment<OrderTrackHistoryFragmentBinding, OrderTrackHistoryViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_order_track_history
    override val viewModel: OrderTrackHistoryViewModel by viewModels {
        viewModelFactory
    }

    lateinit var orderTrackHistoryListAdapter: OrderTrackHistoryListAdapter
    lateinit var orderDetailsProductListAdapter: OrderDetailsProductListAdapter
    lateinit var order: SalesData

    val args: OrderTrackHistoryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        order = args.order
        viewDataBinding.toolbar.title = order.OurReference

        viewDataBinding.name.text = order.customer?.name
        viewDataBinding.email.text = order.customer?.email
        viewDataBinding.address.text = order.customer?.address

        orderDetailsProductListAdapter = OrderDetailsProductListAdapter(appExecutors) {

        }

        viewDataBinding.productRecycler.setHasFixedSize(true)
        viewDataBinding.productRecycler.adapter = orderDetailsProductListAdapter
        orderDetailsProductListAdapter.submitList(order.details)

//        viewDataBinding.vatTax = order.tax_type_total?.toString() ?: "0"
//        viewDataBinding.discount = order.discount_type_total?.toString() ?: "0"
        viewDataBinding.totalPrice = order.grand_total?.toString() ?: "0"
        viewDataBinding.totalPaid = order.paid_amount?.toString() ?: "0"
        viewDataBinding.totalDue = order.due_amount?.toString() ?: "0"

        orderTrackHistoryListAdapter = OrderTrackHistoryListAdapter (appExecutors)

        viewDataBinding.trackRecycler.setHasFixedSize(true)
        viewDataBinding.trackRecycler.adapter = orderTrackHistoryListAdapter

        val trackHistory = ArrayList<OrderTrackHistory>()
        var i = 0
        while (i < 8) {
            var isActive = false
            if (i == 0) isActive = true
            trackHistory.add(OrderTrackHistory(i, "Jan 02, 2021","10:05 AM",
                "Arrived at Delivery Facility in SAN FRANCISCO GATEWAY - USA",
                "SAN FRANCISCO GATEWAY,CA - US, United States", isActive))
            i++
        }

        orderTrackHistoryListAdapter.submitList(trackHistory)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                navController.navigateUp()
            }
        }
        return true
    }
}