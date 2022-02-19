package com.rtchubs.pharmaerp.ui.otp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.databinding.OtpBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class OtpFragment  : BaseFragment<OtpBinding, OtpViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_otp
    override val viewModel: OtpViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.btnVerify.setOnClickListener {
        }
    }
}