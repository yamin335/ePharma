package com.rtchubs.pharmaerp.ui.wallet

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.WalletFragmentBinding
import com.rtchubs.pharmaerp.models.topup.TopUpHelper
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.home.PaymentMethodListAdapter

class WalletFragment : BaseFragment<WalletFragmentBinding, WalletViewModel>() {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_wallet
    override val viewModel: WalletViewModel by viewModels {
        viewModelFactory
    }

    lateinit var paymentListAdapter: PaymentMethodListAdapter
//    lateinit var mFusedLocationClient: FusedLocationProviderClient
//    lateinit var permissionRequestLauncher: ActivityResultLauncher<Array<String>>
//    lateinit var locationManager: LocationManager
//    lateinit var qrCodeScannerLauncher: ActivityResultLauncher<Intent>

    val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CAMERA
    )

    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        mActivity.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //registerToolbar(viewDataBinding.toolbar)

//        viewModel.giftPointStoreResponse.observe(viewLifecycleOwner, Observer { rewards ->
//            rewards?.let {
//                showSuccessToast(requireContext(), "You have earned ${it.reward} gift points!")
//                viewModel.giftPointStoreResponse.postValue(null)
//            }
//        })
//
//        locationManager = requireActivity().getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
//
//        permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
//            val granted = permissions.entries.all {
//                it.value == true
//            }
//
//            if (granted) {
//                getLastLocation()
//            } else {
//                val shouldShowPermissionRationaleDialog = PermissionUtils.checkPermissionRationale(
//                    requireActivity() as AppCompatActivity,
//                    requiredPermissions)
//
//                if (shouldShowPermissionRationaleDialog) {
//                    val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.YesCallback{
//                        override fun onYes() {
//                            permissionRequestLauncher.launch(requiredPermissions)
//                        }
//                    }, "Allow Permissions!", "Allow location and camera permissions to " +
//                            "use this feature.\n\nDo you want to allow permission?")
//                    explanationDialog.show(childFragmentManager, "#call_permission_dialog")
//                } else {
//                    val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.YesCallback{
//                        override fun onYes() {
//                            PermissionUtils.goToSettings(requireContext(), BuildConfig.APPLICATION_ID)
//                        }
//                    }, "Allow Permissions!", "Allow location and camera permissions to " +
//                            "use this feature.\n\nDo you want to allow permission?")
//                    explanationDialog.show(childFragmentManager, "#call_permission_dialog")
//                }
//            }
//        }
//
//        qrCodeScannerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
//            val intent = result.data ?: return@registerForActivityResult
//            if (intent.hasExtra("barcode_result")) {
//                val qrCodeData = intent.getStringExtra("barcode_result") ?: ""
//                if (qrCodeData.isNotBlank()) {
//                    val dataArray = qrCodeData.split(",")
//                    if (dataArray.size >= 3) {
//                        viewModel.saveGiftPoints(GiftPointStoreBody(dataArray[0].toInt(), 8, "Test by YAMIN"))
//                    } else {
//                        showErrorToast(requireContext(), "Invalid QR Code!")
//                    }
//                } else {
//                    showErrorToast(requireContext(), "Invalid QR Code!")
//                }
//            }
//        }

//        viewDataBinding.btnEarnPoint.setOnClickListener {
//            if (PermissionUtils.checkPermission(
//                    requireActivity() as AppCompatActivity,
//                    requiredPermissions
//                )) {
//                getLastLocation()
//            } else {
//                permissionRequestLauncher.launch(requiredPermissions)
//            }
//        }
//
        viewDataBinding.btnShow.setOnClickListener {
            navigateTo(WalletFragmentDirections.actionWalletFragmentToGiftPointRequestListFragment())
        }

        viewDataBinding.cardTopUp.setOnClickListener {
            navController.navigate(WalletFragmentDirections.actionWalletFragmentToTopUpMobileFragment(
                TopUpHelper()
            ))
        }

        val token = preferencesHelper.getAccessTokenHeader()

        paymentListAdapter = PaymentMethodListAdapter(appExecutors) {
            //navController.navigate(HomeFragmentDirections.actionBooksToChapterList(it))
        }

//        viewModel.slideDataList.forEach { slideData ->
//            val slide = SliderView(requireContext())
//            slide.sliderTextTitle = slideData.textTitle
//            slide.sliderTextDescription = slideData.descText
//            slide.sliderImage(slideData.slideImage)
//            viewDataBinding.sliderLayout.addSlider(slide)
//        }

        paymentListAdapter.submitList(viewModel.paymentMethodList)
        viewDataBinding.recyclerPaymentMethods.adapter = paymentListAdapter

        paymentListAdapter.onClicked.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.id == "-1") {
                    /**
                     * add payment method
                     */
                    val action = WalletFragmentDirections.actionWalletFragmentToAddPaymentMethodsFragment()
                    navController.navigate(action)
                }
            }
        })
    }

//    @SuppressLint("MissingPermission")
//    private fun requestForLocation() {
//        val request = LocationRequest.create()
//        request.interval = 1000
//        request.fastestInterval = 500
//
//        mFusedLocationClient.requestLocationUpdates(request, object : LocationCallback() {
//            override fun onLocationAvailability(locationAvailability: LocationAvailability?) {
//                super.onLocationAvailability(locationAvailability)
//                if (locationAvailability != null && locationAvailability.isLocationAvailable) {
//                    try {
//                        currentLocation = mFusedLocationClient.lastLocation?.result
//                        mFusedLocationClient.removeLocationUpdates(this)
//                        scanQRCode()
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                } else {
//                    checkLocationEnableStatus()
//                }
//            }
//
//            override fun onLocationResult(locationResult: LocationResult?) {
//                super.onLocationResult(locationResult)
//                currentLocation = locationResult?.lastLocation
//                mFusedLocationClient.removeLocationUpdates(this)
//                scanQRCode()
//            }
//        }, Looper.myLooper())
//    }
//
//    private fun scanQRCode() {
//        qrCodeScannerLauncher.launch(Intent(requireActivity(), LiveBarcodeScanningActivity::class.java))
//    }
//
//    private fun checkLocationEnableStatus() {
//        var isGpsEnabled = false
//        var isNetworkEnabled = false
//
//        try {
//            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//
//        try {
//            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//
//        if (!isGpsEnabled && !isNetworkEnabled) {
//            showDialog()
//        } else {
//            getLastLocation()
//        }
//    }
//
//    @SuppressLint("MissingPermission")
//    private fun getLastLocation() {
//        mFusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            if (location == null) {
//                requestForLocation()
//            } else {
//                this.currentLocation = location
//                scanQRCode()
//            }
//        }
//    }
//
//    private fun showDialog() {
//        val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.YesCallback{
//            override fun onYes() {
//                try {
//                    val callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//                    startActivity(callGPSSettingIntent)
//                } catch (ex: Exception) {
//                    ex.printStackTrace()
//                }
//            }
//        }, "Turn on GPS!", "Turn on GPS to access location." +
//                "\n\nDo you want to enable now?")
//        explanationDialog.show(childFragmentManager, "#enable_gps_dialog")
//    }
}