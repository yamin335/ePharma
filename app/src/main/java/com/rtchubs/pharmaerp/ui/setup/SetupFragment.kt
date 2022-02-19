package com.rtchubs.pharmaerp.ui.setup

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.databinding.SetupBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class SetupFragment  : BaseFragment<SetupBinding, SetupViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_setup
    override val viewModel: SetupViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewDataBinding.btnProceed.setOnClickListener {
        }

    }
}