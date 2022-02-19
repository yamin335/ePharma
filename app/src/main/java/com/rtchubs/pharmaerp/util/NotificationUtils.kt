package com.rtchubs.pharmaerp.util

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.rtchubs.pharmaerp.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object NotificationUtils {
    // notification icon
    const val ATTENDANCE_NOTIFICATION_ID = 9287639
    const val locationMarkerIcon: Int = R.drawable.ic_baseline_location_on_24
    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder {
        // User invisible channel ID
        val mChannelId = context.getString(R.string.location_tracking_notification_channel_id)
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            prepareChannel(context, mChannelId)
        }
        return NotificationCompat.Builder(context, mChannelId)
    }

    // Set the Channel ID for Android O.
    @TargetApi(VERSION_CODES.O)
    fun prepareChannel(context: Context, mChannelId: String?) {
        // Notification channel settings
        // Default Notification Priority
        val mChannelImportance = NotificationManager.IMPORTANCE_HIGH
        val mChannelLockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC

        // User visible channel name
        val mChannelName = context.getString(R.string.location_tracking_notification_channel_name)
        // User visible channel description
        val mChannelDescription =
            context.getString(R.string.location_tracking_notification_channel_description)
        val notificationManager = context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
        var mChannel = notificationManager.getNotificationChannel(mChannelId)
        if (mChannel == null) {
            mChannel = NotificationChannel(mChannelId, mChannelName, mChannelImportance)
            mChannel.description = mChannelDescription
            mChannel.enableLights(true)
            mChannel.lightColor = Color.GREEN
            mChannel.enableVibration(false)
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            mChannel.setShowBadge(true)
            mChannel.lockscreenVisibility = mChannelLockScreenVisibility
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    /**
     * Returns the [NotificationCompat] used as part of the foreground service.
     */
    fun getAttendanceNotification(
        context: Context,
        title: String?,
        body: String?,
        time: Long,
        servicePendingIntent: PendingIntent?,
        activityPendingIntent: PendingIntent?
    ): Notification {
        val mBuilder = getNotificationBuilder(context)
        return mBuilder
            .addAction(R.drawable.ic_baseline_location_on_24, context.getString(R.string.open_app),
                activityPendingIntent)
            .addAction(R.drawable.ic_baseline_location_off_24, context.getString(R.string.check_out),
                servicePendingIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setSmallIcon(locationMarkerIcon)
            .setTicker(title)
            .setWhen(time)
            .setColor(context.getColor(R.color.themeColor))
            .build()
    }

    fun showAttendanceLocationUpdateServiceNotification(
        context: Context,
        title: String?,
        body: String?,
        time: Long,
        servicePendingIntent: PendingIntent?,
        activityPendingIntent: PendingIntent?
    ) {
        val notification = getAttendanceNotification(context, title, body, time, servicePendingIntent, activityPendingIntent)
        val notificationManager = context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(ATTENDANCE_NOTIFICATION_ID, notification)
    }

    /**
     * Convert time stamp into human
     * readable time
     */
    fun getTimeMilliSec(timeStamp: String): Long {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        try {
            val date = format.parse(timeStamp)
            if (date != null) return date.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0
    }
}