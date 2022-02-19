package com.rtchubs.pharmaerp.util

import com.rtchubs.pharmaerp.BuildConfig

object AppConstants {
    const val PREF_NAME = "QPayCredentials" //"PharmaERPPrefs"
    const val COUNTRY_CODE = "+88"
    const val TERMS_AND_CONDITIONS_URL =
    "https://rtchubs.com/privacypolicy/qrbarcodescanner"
    const val START_TIME_IN_MILLI_SECONDS = 300000L
    const val commonErrorMessage = "Something wrong! please try again later."
    const val serverConnectionErrorMessage = "Can not connect to server! Please try again later."
    const val otpWaitMessage = "Please wait until you get an OTP code!"
    const val registrationSuccessMessage = "Welcome! to QPay. Please login to continue."
    const val saveSuccessfulMessage = "Saved Successfully!"
    const val downloadedPdfFiles = "downloaded_pdf_files"

    private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID

    const val ACTION_BROADCAST_LOCATION_UPDATE = "$PACKAGE_NAME.broadcast"
    const val EXTRA_LOCATION_UPDATE = "$PACKAGE_NAME.location"
    const val EXTRA_STARTED_FROM_NOTIFICATION = "$PACKAGE_NAME.started_from_notification"

    const val orderPending = "Pending"
    const val orderPicked = "Picked"
    const val orderShipped = "Shipped"
    const val orderDelivered = "Delivered"
    const val orderCancelled = "Cancelled"

    const val CHECK_IN = "punchin"
    const val CHECK_OUT = "punchout"
}