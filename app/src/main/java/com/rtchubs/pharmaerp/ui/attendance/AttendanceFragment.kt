package com.rtchubs.pharmaerp.ui.attendance

import android.Manifest
import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rtchubs.pharmaerp.BR
import com.rtchubs.pharmaerp.BuildConfig
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.AllCustomersFragmentBinding
import com.rtchubs.pharmaerp.databinding.AttendanceFragmentBinding
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestBody
import com.rtchubs.pharmaerp.models.customers.Customer
import com.rtchubs.pharmaerp.models.login.Merchant
import com.rtchubs.pharmaerp.ui.AttendanceLocationUpdateServiceCallback
import com.rtchubs.pharmaerp.ui.LogoutHandlerCallback
import com.rtchubs.pharmaerp.ui.NavDrawerHandlerCallback
import com.rtchubs.pharmaerp.ui.common.BaseFragment
import com.rtchubs.pharmaerp.ui.common.CommonAlertDialog
import com.rtchubs.pharmaerp.util.AppConstants
import com.rtchubs.pharmaerp.util.AppConstants.ACTION_BROADCAST_LOCATION_UPDATE
import com.rtchubs.pharmaerp.util.AppConstants.EXTRA_LOCATION_UPDATE
import com.rtchubs.pharmaerp.util.PermissionUtils

class AttendanceFragment : BaseFragment<AttendanceFragmentBinding, AttendanceViewModel>(), SharedPreferences.OnSharedPreferenceChangeListener {
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_attendance
    override val viewModel: AttendanceViewModel by viewModels {
        viewModelFactory
    }

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private lateinit var locationUpdateReceiver: BroadcastReceiver

    lateinit var permissionRequestLauncher: ActivityResultLauncher<Array<String>>

    private val requiredPermissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    private var locationUpdateCallback: AttendanceLocationUpdateServiceCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AttendanceLocationUpdateServiceCallback) {
            locationUpdateCallback = context
        } else {
            throw RuntimeException("$context must implement LoginHandlerCallback")
        }
    }

    override fun onDetach() {
        super.onDetach()
        locationUpdateCallback = null
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(locationUpdateReceiver,
            IntentFilter(ACTION_BROADCAST_LOCATION_UPDATE))
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(locationUpdateReceiver)
        super.onPause()
    }

    override fun onStart() {
        super.onStart()
        requireContext().getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
            .registerOnSharedPreferenceChangeListener(this)

        // Restore the state of the buttons when the activity (re)launches.
        setButtonsState(preferencesHelper.isUpdatingLocation)
    }

    override fun onStop() {
        requireContext().getSharedPreferences(AppConstants.PREF_NAME, Context.MODE_PRIVATE)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationUpdateReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val location = intent?.getParcelableExtra<Location>(EXTRA_LOCATION_UPDATE)
                if (location != null) {
//                    Toast.makeText(
//                        context, "Current Location(${location.latitude}, ${location.longitude})",
//                        Toast.LENGTH_SHORT
//                    ).show()
                }
            }
        }

        permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }

            if (granted) {
                // Permission was granted.
                locationUpdateCallback?.onCheckIn()
            } else {
                val shouldShowPermissionRationaleDialog = PermissionUtils.checkPermissionRationale(
                    requireActivity() as AppCompatActivity,
                    requiredPermissions)

                if (shouldShowPermissionRationaleDialog) {
                    val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.ActionCallback{
                        override fun onYes() {
                            permissionRequestLauncher.launch(requiredPermissions)
                        }

                        override fun onNo() {
                            // Permission denied.
                            setButtonsState(false)
                        }
                    }, "Allow Permissions!", "Allow location permissions to " +
                            "use this feature.\n\nDo you want to allow permission?")
                    explanationDialog.show(childFragmentManager, "#call_permission_dialog")
                } else {
                    val explanationDialog = CommonAlertDialog(object :  CommonAlertDialog.ActionCallback{
                        override fun onYes() {
                            PermissionUtils.goToSettings(requireContext(), BuildConfig.APPLICATION_ID)
                        }

                        override fun onNo() {
                            // Permission denied.
                            setButtonsState(false)
                        }
                    }, "Allow Permissions!", "Allow location permissions to " +
                            "use this feature.\n\nDo you want to allow permission?")
                    explanationDialog.show(childFragmentManager, "#call_permission_dialog")
                }
            }
        }

        viewDataBinding.btnCheckIn.setOnClickListener {
            if (checkNetworkStatus()) {
                if (PermissionUtils.checkPermission(
                        requireActivity() as AppCompatActivity,
                        requiredPermissions
                    )) {
                    locationUpdateCallback?.onCheckIn()
                } else {
                    permissionRequestLauncher.launch(requiredPermissions)
                }
            }
        }

        viewDataBinding.btnCheckOut.setOnClickListener {
            locationUpdateCallback?.onCheckOut()
        }

        // Check that the user hasn't revoked permissions by going to Settings.
        if (preferencesHelper.isUpdatingLocation) {
            if (!PermissionUtils.checkPermission(
                    requireActivity() as AppCompatActivity,
                    requiredPermissions
                )) {
                permissionRequestLauncher.launch(requiredPermissions)
            }
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        // Update the buttons state depending on whether location updates are being requested.
        setButtonsState(
            preferencesHelper.isUpdatingLocation
        )
    }


    private fun setButtonsState(requestingLocationUpdates: Boolean) {
        if (requestingLocationUpdates) {
            viewDataBinding.btnCheckIn.isEnabled = false
            viewDataBinding.btnCheckOut.isEnabled = true
        } else {
            viewDataBinding.btnCheckIn.isEnabled = true
            viewDataBinding.btnCheckOut.isEnabled = false
        }
    }
}