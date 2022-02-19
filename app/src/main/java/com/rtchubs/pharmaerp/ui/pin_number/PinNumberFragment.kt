package com.rtchubs.pharmaerp.ui.pin_number

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.api.TokenInformation
import com.rtchubs.pharmaerp.databinding.PinNumberBinding
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.util.hideKeyboard
import com.rtchubs.pharmaerp.util.showErrorToast
import com.rtchubs.pharmaerp.util.showWarningToast
import org.json.JSONObject

class PinNumberFragment : BaseFragment<PinNumberBinding, PinNumberViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_pin_number

    override val viewModel: PinNumberViewModel by viewModels { viewModelFactory }

    val args: PinNumberFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateStatusBarBackgroundColor("#1E4356")
        registerToolbar(viewDataBinding.toolbar)

        val helper = args.registrationHelper

        if (helper.isRegistered) {
            viewDataBinding.linearReTypePin.visibility = View.GONE
        }

        viewModel.defaultResponse.observe(viewLifecycleOwner, Observer { response ->
            response?.let {
                when {
                    it.isSuccess == true -> {
                        val data = JSONObject(response.body.toString())
                        var tokenInfo = TokenInformation(
                            data.getString("access_token"), data.getString("expires_in").toLong()
                            , data.getString("refresh_token"), data.getString("token_type")
                        )
                        preferencesHelper.saveToken(tokenInfo)
                        //val action = PinNumberFragmentDirections.actionPinNumberFragmentToHome()
                        //navController.navigate(action)
                    }
                    it.isSuccess == false && it.errorMessage != null -> {
                        showWarningToast(mContext, it.errorMessage)
                    }
                    else -> {
                        showWarningToast(mContext, getString(R.string.something_went_wrong))
                    }
                }
            }
        })

        viewModel.pin.observe(viewLifecycleOwner, Observer { pin ->
            pin?.let {
                if (helper.isRegistered) {
                    viewDataBinding.btnSubmit.isEnabled = pin.length == 6
                } else {
                    viewDataBinding.btnSubmit.isEnabled = pin.length == 6 && viewModel.rePin.value?.length == 6
                }
            }
        })

        viewModel.rePin.observe(viewLifecycleOwner, Observer { rePin ->
            rePin?.let {
                viewDataBinding.btnSubmit.isEnabled = viewModel.pin.value?.length == 6 && rePin.length == 6
            }
        })

        viewDataBinding.btnSubmit.setOnClickListener {
            hideKeyboard()
            var pin = ""
            var rePin = ""

            viewModel.pin.value?.let {
                pin = it
            }

            viewModel.rePin.value?.let {
                rePin = it
            }

            when {
                helper.isRegistered && pin.isNotBlank() && rePin.isBlank() -> {
                    viewModel.connectToken(
                        helper.mobile,
                        pin,
                        "password",
                        "offline_access",
                        Build.ID,
                        Build.MANUFACTURER,
                        Build.MODEL,
                        "qpayandroid",
                        "07A96yr@!1t8r",
                        helper.otp
                    )
                }
                pin != rePin && !helper.isRegistered -> {
                    viewDataBinding.etRepin.requestFocus()
                    showErrorToast(requireContext(), "Both pin number does not match")
                }
                pin.isNotBlank() && rePin.isNotBlank() && pin == rePin && !helper.isRegistered -> {
                    helper.pinNumber = pin
                    val action =
                        PinNumberFragmentDirections.actionPinNumberFragmentToPermissionsFragment(
                            helper
                        )
                    navController.navigate(action)
                }
            }
        }
    }
}