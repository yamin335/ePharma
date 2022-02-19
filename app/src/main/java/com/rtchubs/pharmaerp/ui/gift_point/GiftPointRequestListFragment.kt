package com.rtchubs.pharmaerp.ui.gift_point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.GiftPointRequestListFragmentBinding
import com.rtchubs.pharmaerp.models.GiftPointRequest
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class GiftPointRequestListFragment : BaseFragment<GiftPointRequestListFragmentBinding, GiftPointRequestListViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_gift_point_request_list
    override val viewModel: GiftPointRequestListViewModel by viewModels {
        viewModelFactory
    }

    lateinit var pointHistoryListAdapter: GiftPointRequestListAdapter

    override fun onResume() {
        super.onResume()
        viewModel.getGiftPointsHistory(preferencesHelper.merchantId)
        visibleGoneEmptyView()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            android.R.id.home -> {
//                navController.navigateUp()
//            }
//        }
//        return true
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        pointHistoryListAdapter = GiftPointRequestListAdapter(appExecutors) {
            navigateTo(GiftPointRequestListFragmentDirections.actionGiftPointRequestListFragmentToGiftPointRequestListDetailsFragment(it))
        }

        viewDataBinding.historyRecycler.adapter = pointHistoryListAdapter

        viewModel.giftPointsHistoryList.observe(viewLifecycleOwner, Observer {
            giftPointHistoryList = it as ArrayList<GiftPointRequest>
            pointHistoryListAdapter.submitList(giftPointHistoryList)
            visibleGoneEmptyView()
        })
    }

    private fun visibleGoneEmptyView() {
        if (giftPointHistoryList.isEmpty()) {
            viewDataBinding.container.visibility = View.GONE
            viewDataBinding.emptyView.visibility = View.VISIBLE
        } else {
            viewDataBinding.container.visibility = View.VISIBLE
            viewDataBinding.emptyView.visibility = View.GONE
        }
    }

    companion object {
        var giftPointHistoryList = ArrayList<GiftPointRequest>()
    }
}