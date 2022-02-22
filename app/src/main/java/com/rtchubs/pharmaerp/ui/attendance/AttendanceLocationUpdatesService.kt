package com.rtchubs.pharmaerp.ui.attendance

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import com.rtchubs.pharmaerp.api.*
import com.rtchubs.pharmaerp.models.attendance.AttendanceLocationUpdateRequestBody
import com.rtchubs.pharmaerp.models.attendance.CheckInOutRequestBody
import com.rtchubs.pharmaerp.prefs.PreferencesHelper
import com.rtchubs.pharmaerp.repos.AttendanceRepository
import com.rtchubs.pharmaerp.ui.MainActivity
import com.rtchubs.pharmaerp.util.AppConstants
import com.rtchubs.pharmaerp.util.AppConstants.ACTION_BROADCAST_LOCATION_UPDATE
import com.rtchubs.pharmaerp.util.AppConstants.EXTRA_LOCATION_UPDATE
import com.rtchubs.pharmaerp.util.AppConstants.EXTRA_STARTED_FROM_NOTIFICATION
import com.rtchubs.pharmaerp.util.NetworkUtils
import com.rtchubs.pharmaerp.util.NotificationUtils
import dagger.android.AndroidInjection
import dagger.android.DaggerService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 *
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 *
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that service is removed.
 */
class AttendanceLocationUpdatesService : DaggerService() {
    private val mBinder: IBinder = LocalBinder()

    @Inject
    lateinit var preference: PreferencesHelper

    @Inject
    lateinit var attendanceRepository: AttendanceRepository

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private var mChangingConfiguration = false
    //private var mNotificationManager: NotificationManager? = null

    /**
     * Contains parameters used by [com.google.android.gms.location.FusedLocationProviderApi].
     */
    private lateinit var mLocationRequest: LocationRequest

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    /**
     * Callback for changes in location.
     */
    private lateinit var mLocationCallback: LocationCallback
    private lateinit var mServiceHandler: Handler

    /**
     * The current location.
     */
    private var mLocation: Location? = null

    private val locationText: String get() {
        return "You are now checked in"
    }

    private val notificationTitle: String get() {
        val formatter = SimpleDateFormat("dd-MMM-yyyy hh:mm a", Locale.US)
        val currentTime = formatter.format(Date())
        return "Updated At: $currentTime"
    }

    override fun onCreate() {
        AndroidInjection.inject(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val startedFromNotification = intent.getBooleanExtra(
            EXTRA_STARTED_FROM_NOTIFICATION,
            false
        )

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            try {
                mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                        val user = preference.getUser()
                        checkOut(this, CheckInOutRequestBody(
                            user.id?.toString(), AppConstants.CHECK_OUT,
                            mLocation?.longitude.toString(), mLocation?.latitude.toString(),
                            "manual", "Checked Out"
                        ))
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
            } catch (unlikely: SecurityException) {
                Log.e(TAG, "Lost location permission.$unlikely")
            }
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }

    override fun onBind(intent: Intent): IBinder {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.i(
            TAG,
            "Last client unbound from service"
        )
        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && preference.isUpdatingLocation) {
            startForeground(NotificationUtils.ATTENDANCE_NOTIFICATION_ID,
                NotificationUtils.getAttendanceNotification(
                    this, notificationTitle,
                    locationText, System.currentTimeMillis(),
                    getServicePendingIntent(), getActivityPendingIntent())
            )
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }

    // The PendingIntent that leads to a call to onStartCommand() in this service.
    private fun getServicePendingIntent(): PendingIntent {
        val intent = Intent(this, AttendanceLocationUpdatesService::class.java)

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        return PendingIntent.getService(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // The PendingIntent to launch activity.
    private fun getActivityPendingIntent(): PendingIntent {
        val intent = Intent(this, AttendanceLocationUpdatesService::class.java)

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true)

        // The PendingIntent to launch activity.
        return PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java), 0 or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onDestroy() {
        mServiceHandler.removeCallbacksAndMessages(null)
    }

    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun requestLocationUpdates() {
        preference.isUpdatingLocation = true
        startService(Intent(applicationContext, AttendanceLocationUpdatesService::class.java))
        try {
            Looper.myLooper()?.let {
                Log.i(TAG, "Loop is OK")
                mFusedLocationClient.requestLocationUpdates(
                    mLocationRequest,
                    mLocationCallback, it
                )
            }
        } catch (unlikely: SecurityException) {
            preference.isUpdatingLocation = false
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
            preference.isUpdatingLocation = false
            stopSelf()
        } catch (unlikely: SecurityException) {
            preference.isUpdatingLocation = true
            Log.e(TAG, "Lost location permission. Could not remove updates. $unlikely")
            stopSelf()
        }
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    mLocation = task.result
                } else {
                    Log.w(TAG, "Failed to get location.")
                }
            }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location) {
        mLocation = location

        val user = preference.getUser()
        updateLocationToServer(this, AttendanceLocationUpdateRequestBody(user.id?.toString(),
            mLocation?.latitude.toString(), mLocation?.longitude.toString(), ""))

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST_LOCATION_UPDATE)
        intent.putExtra(EXTRA_LOCATION_UPDATE, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
            NotificationUtils.showAttendanceLocationUpdateServiceNotification(
                this, notificationTitle,
                locationText, System.currentTimeMillis(),
                getServicePendingIntent(), getActivityPendingIntent()
            )
        }
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        val service: AttendanceLocationUpdatesService
            get() = this@AttendanceLocationUpdatesService
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The [Context].
     */
    fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(
            ACTIVITY_SERVICE
        ) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    fun updateLocationToServer(context: Context, attendanceLocationUpdateRequestBody: AttendanceLocationUpdateRequestBody) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
            }

            CoroutineScope(Dispatchers.IO).launch(handler) {
                when (val apiResponse = ApiResponse.create(attendanceRepository.updateCurrentLocation(attendanceLocationUpdateRequestBody))) {
                    is ApiSuccessResponse -> {
                        apiResponse.body.code?.let {
                            Log.i(TAG, "New location: (Latitude: ${attendanceLocationUpdateRequestBody.lat}, Longitude: ${attendanceLocationUpdateRequestBody.lat})")
                        }
                    }
                    is ApiEmptyResponse -> {}
                    is ApiErrorResponse -> {}
                }
            }
        }
    }

    fun checkOut(context: Context, checkInOutRequestBody: CheckInOutRequestBody) {
        if (NetworkUtils.isNetworkConnected(context)) {
            val handler = CoroutineExceptionHandler { _, exception ->
                exception.printStackTrace()
                removeLocationUpdates()
            }

            CoroutineScope(Dispatchers.IO).launch(handler) {
                when (val apiResponse = ApiResponse.create(attendanceRepository.checkInOut(checkInOutRequestBody))) {
                    is ApiSuccessResponse -> {
                        apiResponse.body.data?.let {
                            removeLocationUpdates()
                        }
                    }
                    is ApiEmptyResponse -> {
                        removeLocationUpdates()
                    }
                    is ApiErrorResponse -> {
                        removeLocationUpdates()
                    }
                }
            }
        }
    }

    companion object {
        private val TAG: String = AttendanceLocationUpdatesService::class.java.simpleName

        /**
         * The desired interval for location updates. Inexact. Updates may be more or less frequent.
         */
        private const val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 10*60*1000

        /**
         * The fastest rate for active location updates. Updates will never be more frequent
         * than this value.
         */
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 4 * (UPDATE_INTERVAL_IN_MILLISECONDS / 5)
    }
}