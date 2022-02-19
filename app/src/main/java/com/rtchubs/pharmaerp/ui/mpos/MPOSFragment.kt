package com.rtchubs.pharmaerp.ui.mpos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.MPOSFragmentBinding
import com.rtchubs.pharmaerp.models.order.FilterDate
import com.rtchubs.pharmaerp.models.order.GetOrderListRequestBody
import com.rtchubs.pharmaerp.models.order.SalesData
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MPOSFragment : BaseFragment<MPOSFragmentBinding, MPOSViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_mpos
    override val viewModel: MPOSViewModel by viewModels {
        viewModelFactory
    }

    lateinit var mposOrderListAdapter: MPOSOrderListAdapter

    override fun onResume() {
        super.onResume()
        mposOrderListAdapter.submitList(orderList)
        showHideDataView()
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(
            Date()
        )
        val filterDate = FilterDate(today, today)
        viewModel.getOrderList(GetOrderListRequestBody("status", "Pending", filterDate, false), 1)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mposOrderListAdapter = MPOSOrderListAdapter(appExecutors) {
            navigateTo(MPOSFragmentDirections.actionMPOSFragmentToMPOSOrderDetailsFragment(it))
        }

        viewDataBinding.orderListRecycler.adapter = mposOrderListAdapter

        viewModel.orderItems.observe(viewLifecycleOwner, Observer {
            orderList = it as ArrayList<SalesData>
            mposOrderListAdapter.submitList(orderList)
            showHideDataView()
        })

        viewDataBinding.btnCreateOrder.setOnClickListener {
            navigateTo(MPOSFragmentDirections.actionMPOSFragmentToCreateMPOSOrderFragment())
        }
    }

    private fun showHideDataView() {
        if (orderList.isEmpty()) {
            viewDataBinding.container.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.container.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }

    companion object {
        var orderList: ArrayList<SalesData> = ArrayList()
    }
}