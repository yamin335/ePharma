package com.rtchubs.pharmaerp.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.google.android.material.navigation.NavigationView
import com.rtchubs.pharmaerp.R
import com.rtchubs.pharmaerp.databinding.MainActivityBinding
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestBody
import com.rtchubs.pharmaerp.prefs.PreferencesHelper
import com.rtchubs.pharmaerp.ui.attendance.AttendanceLocationUpdatesService
import com.rtchubs.pharmaerp.ui.common.CommonAlertDialog
import com.rtchubs.pharmaerp.ui.common.CustomLoadingDialogFragment
import com.rtchubs.pharmaerp.ui.home.Home2FragmentDirections
import com.rtchubs.pharmaerp.util.AppConstants
import com.rtchubs.pharmaerp.util.hideKeyboard
import com.rtchubs.pharmaerp.util.shouldCloseDrawerFromBackPress
import com.rtchubs.pharmaerp.util.showErrorToast
import dagger.android.support.DaggerAppCompatActivity
import java.io.File
import javax.inject.Inject

interface LoginHandlerCallback {
    fun onLoggedIn()
}

interface AttendanceLocationUpdateServiceCallback {
    fun onCheckIn()
    fun onCheckOut()
}

interface LogoutHandlerCallback {
    fun onLoggedOut()
}

interface NavDrawerHandlerCallback {
    fun toggleNavDrawer()
}

class MainActivity :
    DaggerAppCompatActivity(), LogoutHandlerCallback,
    NavDrawerHandlerCallback, NavigationHost,
    NavigationView.OnNavigationItemSelectedListener,
    AttendanceLocationUpdateServiceCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    private val viewModel: MainActivityViewModel by viewModels {
        // Get the ViewModel.
        viewModelFactory
    }

    lateinit var binding: MainActivityBinding

    private var currentNavController: LiveData<NavController>? = null

    private var currentNavHostFragment: LiveData<NavHostFragment>? = null

    private val fragmentWithoutBottomNav = setOf(
        R.id.viewPagerFragment,
        R.id.signInFragment,
        R.id.termsAndConditions,
        R.id.otpSignInFragment,
        R.id.pinNumberFragment,
        R.id.profileSignInFragment,
        R.id.ARLocationFragment,
        R.id.addProductFragment,
        R.id.addCustomerFragment,
        R.id.createOrderFragment,
        R.id.selectCustomerFragment
    )

    private var navigatedFromDashboard = false

    private var loginNavHostFragment: NavHostFragment? = null

    // A reference to the service used to get location updates.
    private var mService: AttendanceLocationUpdatesService? = null

    // Tracks the bound state of the service.
    private var mBound = false

    // Monitors the state of the connection to the service.
    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: AttendanceLocationUpdatesService.LocalBinder = service as AttendanceLocationUpdatesService.LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    // The Fused Location Provider provides access to location APIs.
    private val mFusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    // Allows class to cancel the location request if it exits the activity.
    // Typically, you use one cancellation source per lifecycle.
    private var cancellationTokenSource = CancellationTokenSource()

    lateinit var locationManager: LocationManager

    private lateinit var loader: CustomLoadingDialogFragment

    override fun onStart() {
        super.onStart()
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.

        bindService(
            Intent(this, AttendanceLocationUpdatesService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE
        )

        cancellationTokenSource = CancellationTokenSource()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection)
            mBound = false
        }
        super.onStop()

        // Cancels location request (if in flight).
        cancellationTokenSource.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.mainContainer.showBottomNav = true
        binding.drawerNavigation.setNavigationItemSelectedListener(this)

        loader = CustomLoadingDialogFragment()

//        binding.mainContainer.btnLiveChat.setOnClickListener {
//            startActivity(Intent(this, LiveChatActivity::class.java))
//            overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
//        }

        // Setup multi-backStack supported bottomNav
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

        locationManager = getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager

        viewModel.checkInOutResponse.observe(this, Observer {
            when(it.first) {
                AppConstants.CHECK_IN -> {
                    mService?.requestLocationUpdates()
                }
                AppConstants.CHECK_OUT -> {
                    mService?.removeLocationUpdates()
                }
            }
        })
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {

        val navGraphIds = listOf(
            R.navigation.customers_nav_graph,
            R.navigation.products_nav_graph,
            R.navigation.mpos_nav_graph,
            R.navigation.attendance_nav_graph,
            R.navigation.more_nav_graph
        )

        // Setup the bottom navigation view with a payment_graph of navigation graphs
        val (controller, navHost) = binding.mainContainer.bottomNav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
//            appBarConfiguration = AppBarConfiguration(
//                navGraph = navController.graph,
//                drawerLayout = drawer_layout
//            )
            // Set up ActionBar
//            setSupportActionBar(toolbar)
//            setupActionBarWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                hideKeyboard()
                binding.mainContainer.showBottomNav = destination.id !in fragmentWithoutBottomNav
            }

//            setupActionBarWithNavController(navController)
        })
        currentNavController = controller
        currentNavHostFragment = navHost
    }

    override fun onBackPressed() {
        /**
         * If the drawer is open, the behavior changes based on the API level.
         * When gesture nav is enabled (Q+), we want back to exit when the drawer is open.
         * When button navigation is enabled (on Q or pre-Q) we want to close the drawer on back.
         */
        if (binding.navDrawer.isDrawerOpen(binding.drawerNavigation) && binding.navDrawer.shouldCloseDrawerFromBackPress()) {
            binding.navDrawer.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun toggleNavDrawer() {
        if (binding.navDrawer.isDrawerOpen(binding.drawerNavigation)) {
            binding.navDrawer.closeDrawer(GravityCompat.START)
        } else {
            binding.navDrawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false || super.onSupportNavigateUp()
    }

    override fun onLoggedOut() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out)
        finish()
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
        currentNavController?.value?.let {
            setupActionBarWithNavController(it)
        }
    }

    companion object {
        const val REQUEST_APP_UPDATE = 557
        private const val ERROR_DIALOG_REQUEST_CODE = 1

        private const val NAV_ID_NONE = -1

        /** Use external media if it is available, our app's file directory otherwise */
        fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, appContext.resources.getString(R.string.app_name)).apply { mkdirs() } }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_exams -> {
                currentNavController?.value?.let { navController ->
                    when (navController.graph.id) {
                        R.id.home_nav -> {
                            navController.navigate(Home2FragmentDirections.actionHome2FragmentToExamsNavGraph())
                        }
                    }
                }
            }

            R.id.nav_settings -> {
            }

            R.id.nav_info -> {
            }
        }

        //close navigation drawer
        binding.navDrawer.closeDrawer(GravityCompat.START);
        return true
    }

    private fun showLoader() {
        loader.show(supportFragmentManager, "#custom_loading_dialog")
    }

    private fun hideLoader() {
        loader.dismiss()
    }

    override fun onCheckIn() {
        requestForLocation(AppConstants.CHECK_IN)
    }

    override fun onCheckOut() {
        requestForLocation(AppConstants.CHECK_OUT)
    }

    @SuppressLint("MissingPermission")
    private fun requestForLocation(checkInOutType: String) {
        showLoader()

        // Returns a single current location fix on the device. Unlike getLastLocation() that
        // returns a cached location, this method could cause active location computation on the
        // device. A single fresh location will be returned if the device location can be
        // determined within reasonable time (tens of seconds), otherwise null will be returned.
        //
        // Both arguments are required.
        // PRIORITY type is self-explanatory. (Other options are PRIORITY_BALANCED_POWER_ACCURACY,
        // PRIORITY_LOW_POWER, and PRIORITY_NO_POWER.)
        // The second parameter, [CancellationToken] allows the activity to cancel the request
        // before completion.
        val currentLocationTask: Task<Location> = mFusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )

        currentLocationTask.addOnCompleteListener { task: Task<Location> ->
            if (task.isSuccessful && task.result != null) {
                val result: Location = task.result
                val user = preferencesHelper.getUser()
                viewModel.checkInOut(
                    CheckInOutRequestBody(
                        user.id?.toString(), checkInOutType,
                        result.longitude.toString(), result.latitude.toString(),
                        "manual", "Checked In"
                    ), checkInOutType
                )
            } else {
                val exception = task.exception
                exception?.printStackTrace()
                showErrorToast(this, exception?.localizedMessage ?: "Can not retrieve location")
                checkLocationEnableStatus()
            }
            hideLoader()
        }
    }

    private fun checkLocationEnableStatus() {
        var isGpsEnabled = false
        var isNetworkEnabled = false

        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        try {
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        if (!isGpsEnabled && !isNetworkEnabled) {
            showLocationSettingDialog()
        }
    }

    private fun showLocationSettingDialog() {
        val gpsSettingDialog = CommonAlertDialog(object :  CommonAlertDialog.ActionCallback{
            override fun onYes() {
                try {
                    val callGPSSettingIntent = Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(callGPSSettingIntent)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onNo() {

            }
        }, "Turn on GPS!", "Turn on GPS to access location." +
                "\n" + "\n" + "Do you want to enable now?")
        gpsSettingDialog.show(supportFragmentManager, "#gps_setting_dialog")
    }
}