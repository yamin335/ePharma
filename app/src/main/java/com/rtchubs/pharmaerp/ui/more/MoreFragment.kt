package com.rtchubs.pharmaerp.ui.more

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.BuildConfig
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.MoreFragmentBinding
import com.rtchubs.pharmaerp.ui.LogoutHandlerCallback
import com.rtchubs.pharmaerp.ui.NavDrawerHandlerCallback
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.splash.SplashFragment

class MoreFragment : BaseFragment<MoreFragmentBinding, MoreViewModel>() {

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_more

    override val viewModel: MoreViewModel by viewModels { viewModelFactory }

    private var listener: LogoutHandlerCallback? = null

    private var drawerListener: NavDrawerHandlerCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LogoutHandlerCallback) {
            listener = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }

        if (context is NavDrawerHandlerCallback) {
            drawerListener = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        drawerListener = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = preferencesHelper.getUser()
        viewDataBinding.tvName.text = user.name
        viewDataBinding.tvEmployeeID.text = user.employee_id

        viewDataBinding.version.text = "Version ${BuildConfig.VERSION_NAME}"

        viewDataBinding.btnSignOut.setOnClickListener {
            SplashFragment.fromLogout = true
            preferencesHelper.isLoggedIn = false
            listener?.onLoggedOut()
        }

        viewDataBinding.appLogo.setOnClickListener {
            drawerListener?.toggleNavDrawer()
        }

        viewDataBinding.menuMyDevices.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToMyDevicesFragment())
        }

        viewDataBinding.menuMyAccount.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToMyAccountsFragment())
        }

        viewDataBinding.menuProfiles.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToProfilesFragment())
        }

        viewDataBinding.menuOffers.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToOffersNav())
        }

        viewDataBinding.menuOrders.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToOrderNav())
        }

        viewDataBinding.menuTransactions.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToTransactionsFragment())
        }

        viewDataBinding.menuSettings.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToSettingsFragment())
        }

        viewDataBinding.menuGiftPoint.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToGiftPointHistoryFragment())
        }

        viewDataBinding.menuProductStock.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToStockProductsFragment())
        }

        viewDataBinding.menuPurchaseList.setOnClickListener {
            navigateTo(MoreFragmentDirections.actionMoreFragmentToProductPurchaseFragment())
        }

    }

}