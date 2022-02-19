package com.rtchubs.pharmaerp.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.rtchubs.pharmaerp.ui.add_product.PERMISSION_REQUEST_CODE

object PermissionUtils {
    fun isCameraAndGalleryPermissionGranted(fragmentActivity: FragmentActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            when (PackageManager.PERMISSION_GRANTED) {
                fragmentActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                -> {
                    true
                }
                fragmentActivity.checkSelfPermission(Manifest.permission.CAMERA)
                -> {
                    true
                }
                else -> {
                    ActivityCompat.requestPermissions(
                        fragmentActivity,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA),
                        PERMISSION_REQUEST_CODE
                    )
                    false
                }
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    fun isGalleryPermission(fragmentActivity: FragmentActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (fragmentActivity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    fragmentActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            true
        }
    }

    fun isCameraPermission(fragmentActivity: FragmentActivity): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (fragmentActivity.checkSelfPermission(Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    fragmentActivity,
                    arrayOf(Manifest.permission.CAMERA),
                    PERMISSION_REQUEST_CODE
                )
                false
            }
        } else {
            true
        }
    }

    fun checkPermission(activity: AppCompatActivity, permissions: Array<String>): Boolean = permissions.all { permission ->
        activity.checkSelfPermissionCompat(permission) == PackageManager.PERMISSION_GRANTED
    }

    fun checkPermissionRationale(activity: AppCompatActivity, permissions: Array<String>): Boolean = permissions.all { permission ->
        activity.shouldShowRequestPermissionRationaleCompat(permission)
    }

    fun goToSettings(context: Context, packageName: String) {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName")).apply {
            addCategory(Intent.CATEGORY_DEFAULT)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }.also { intent ->
            context.startActivity(intent)
        }
    }
}