package com.rtchubs.pharmaerp.ui.gift_point

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.GiftPointRequestListDetailsFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class GiftPointRequestListDetailsFragment : BaseFragment<GiftPointRequestListDetailsFragmentBinding, GiftPointRequestListDetailsViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_gift_point_request_list_details
    override val viewModel: GiftPointRequestListDetailsViewModel by viewModels {
        viewModelFactory
    }

    val args: GiftPointRequestListDetailsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerToolbar(viewDataBinding.toolbar)

        viewDataBinding.name = args.reward.name
        viewDataBinding.mobile = args.reward.phone
        viewDataBinding.giftPoint = args.reward.reward?.toString() ?: "0"
        viewDataBinding.remarks = args.reward.remarks

        viewDataBinding.btnApprove.setOnClickListener {
            navController.popBackStack()
        }

        viewDataBinding.btnReject.setOnClickListener {
            navController.popBackStack()
        }
    }
}