package com.rtchubs.pharmaerp.ui.home

import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.SetAFragmentBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment

class SetAFragment : BaseFragment<SetAFragmentBinding, SetAViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_set_a

    override val viewModel: SetAViewModel by viewModels { viewModelFactory }
}